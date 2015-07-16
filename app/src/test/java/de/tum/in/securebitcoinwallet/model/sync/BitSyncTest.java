package de.tum.in.securebitcoinwallet.model.sync;

import android.content.Context;
import com.hannesdorfmann.sqlbrite.dao.DaoManager;
import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.Transaction;
import de.tum.in.securebitcoinwallet.model.database.AddressDao;
import de.tum.in.securebitcoinwallet.model.database.TransactionDao;
import de.tum.in.securebitcoinwallet.model.dto.AddressDto;
import de.tum.in.securebitcoinwallet.model.dto.TransactionDto;
import de.tum.in.securebitcoinwallet.model.dto.TransactionOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * @author Hannes Dorfmann
 */

@RunWith(RobolectricTestRunner.class) @Config(emulateSdk = 18) public class BitSyncTest {

  @Test public void markAsSubmitted() {

    Context c = Robolectric.getShadowApplication().getApplicationContext();
    TransactionDao transactionDao = new TransactionDao();
    AddressDao addressDao = new AddressDao();
    new DaoManager(c, "markTransaction.db", 1, addressDao, transactionDao);

    MockBackend backend = new MockBackend();
    BitcoinSync sync = new BitcoinSync(backend, addressDao, transactionDao);

    Transaction t1 = new Transaction();
    t1.setSyncState(Transaction.SYNC_NOT_SUBMITTED);
    t1.setId("t1");
    t1.setAmount(1);
    t1.setAddress("a1");

    Transaction t2 = new Transaction();
    t2.setSyncState(Transaction.SYNC_NOT_SUBMITTED);
    t2.setId("t2");
    t2.setAmount(2);
    t2.setAddress("a2");

    Transaction t3 = new Transaction();
    t3.setSyncState(Transaction.SYNC_OK);
    t3.setId("t3");
    t3.setAmount(3);
    t3.setAddress("a3");

    transactionDao.insertOrUpdate(t1);
    transactionDao.insertOrUpdate(t2);
    transactionDao.insertOrUpdate(t3);

    boolean needSync = sync.syncNotSubmittedTransactions();
    Assert.assertTrue(needSync);

    Transaction q1 = transactionDao.getTransactionById("t1").toBlocking().first();
    Assert.assertEquals(q1.getSyncState(), Transaction.SYNC_WAITING_CONFIRM);

    Transaction q2 = transactionDao.getTransactionById("t2").toBlocking().first();
    Assert.assertEquals(q2.getSyncState(), Transaction.SYNC_WAITING_CONFIRM);

    Transaction q3 = transactionDao.getTransactionById("t3").toBlocking().first();
    Assert.assertEquals(q3.getSyncState(), Transaction.SYNC_OK);
  }

  @Test public void testAddressTransactionUpdate() {

    Context c = Robolectric.getShadowApplication().getApplicationContext();
    TransactionDao transactionDao = new TransactionDao();
    AddressDao addressDao = new AddressDao();
    new DaoManager(c, "updateSyncTransaction.db", 1, addressDao, transactionDao);

    MockBackend backend = new MockBackend();
    BitcoinSync sync = new BitcoinSync(backend, addressDao, transactionDao);

    int transactions = 200;
    List<TransactionDto> transactionDtos = new ArrayList<>(transactions);
    String address = "a1";

    long timestamp = 2003123;
    long newTimestamp = 55555555;
    long outputsSum = 70;

    for (int i = 0; i < transactions; i++) {

      TransactionDto t = new TransactionDto();

      TransactionOutput output = new TransactionOutput();
      output.setValue(20);

      TransactionOutput output2 = new TransactionOutput();
      output2.setValue(30);

      t.setHash("t" + i);
      t.setOutputs(new ArrayList<>(Arrays.asList(output, output2)));
      t.setTimestamp(timestamp);
      t.setTxIndex("txI" + i);

      Transaction transaction = t.toTransaction();
      transaction.setAddress(address);
      transaction.setSyncState(Transaction.SYNC_WAITING_CONFIRM);
      transaction.setName("name" + i);

      // save to database
      transactionDao.insertOrUpdate(transaction);

      transactionDtos.add(t);
    }

    AddressDto aDto = new AddressDto();
    aDto.setAddress(address);
    aDto.setTotalSent(200);
    aDto.setTotalReceived(100);
    aDto.setAmount(300);
    aDto.setTransactions(transactionDtos);
    backend.put(aDto);

    Address a = new Address();
    a.setAddress(address);
    a.setTotalSent(200);
    a.setTotalReceived(100);
    a.setName("MyAddress");
    a.setAmount(300);

    addressDao.insertOrUpdateAddress(a);

    sync.syncAddresses();

    for (int i = 0; i < transactions; i++) {

      Transaction t = transactionDao.getTransactionById("t" + i).toBlocking().first();

      Assert.assertNotNull(t);
      // Assert.assertEquals(t.getSyncState(), t.SYNC_OK);
      Assert.assertEquals(t.getAddress(), address);
      Assert.assertEquals(t.getName(), "name" + i);
    }
  }
}
