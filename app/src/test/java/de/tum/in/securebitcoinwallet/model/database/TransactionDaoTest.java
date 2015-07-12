package de.tum.in.securebitcoinwallet.model.database;

import android.content.Context;
import com.hannesdorfmann.sqlbrite.dao.DaoManager;
import de.tum.in.securebitcoinwallet.model.Transaction;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import rx.observers.TestSubscriber;

/**
 * @author Hannes Dorfmann
 */
@RunWith(RobolectricTestRunner.class) @Config(emulateSdk = 18) public class TransactionDaoTest {

  @Test public void insertUpdateDelete() {

    Context context = Robolectric.getShadowApplication().getApplicationContext();
    TransactionDao dao = new TransactionDao();
    new DaoManager(context, "Test.db", 1, dao);

    int tests = 10;
    String address = "address";

    // Insert
    for (int i = 0; i < tests; i++) {
      String id = "id" + i;
      String name = "name" + i;
      String txIndex = "txIndex" + i;

      Transaction t = new Transaction();
      t.setId(id);
      t.setAmount(i);
      t.setName(name);
      t.setAddress(address);
      t.setSyncState(Transaction.SYNC_NOT_SUBMITTED);
      t.setTimestamp(i);
      t.setTxIndex(txIndex);

      TestSubscriber<Transaction> subscriber = new TestSubscriber<>();
      dao.insertOrUpdate(t).subscribe(subscriber);

      subscriber.assertNoErrors();
      Assert.assertEquals(1, subscriber.getOnCompletedEvents().size());
      Assert.assertEquals(1, subscriber.getOnNextEvents().size());

      Transaction ts = subscriber.getOnNextEvents().get(0);
      Assert.assertTrue(t == ts);
      subscriber.unsubscribe();

      // query by id
      subscriber = new TestSubscriber<>();
      dao.getTransactionById(id).subscribe(subscriber);
      subscriber.assertNoErrors();
      Assert.assertEquals(1, subscriber.getOnNextEvents().size());
      Transaction queried = subscriber.getOnNextEvents().get(0);
      checkTransactionEquals(t, queried);
      subscriber.unsubscribe();

      // query by transaction index
      subscriber = new TestSubscriber<>();
      dao.getTransactionByTxIndex(txIndex).subscribe(subscriber);
      subscriber.assertNoErrors();
      Assert.assertEquals(1, subscriber.getOnNextEvents().size());
      queried = subscriber.getOnNextEvents().get(0);
      checkTransactionEquals(t, queried);
      subscriber.unsubscribe();

      // Query list of addresses
      TestSubscriber<List<Transaction>> addressSub = new TestSubscriber<>();
      dao.getTransactionForAddress(address).subscribe(addressSub);
      addressSub.assertNoErrors();
      Assert.assertEquals(1, addressSub.getOnNextEvents().size());
      List<Transaction> transactions = addressSub.getOnNextEvents().get(0);
      Assert.assertEquals(i + 1, transactions.size());
      checkTransactionEquals(t,
          transactions.get(0)); // First in the list is the last inserted because of ORDER DESC
      addressSub.unsubscribe();
    }

    // Delete transaction
    for (int i = 0; i < tests; i++) {
      String id = "id" + i;
      TestSubscriber<Integer> subscriber = new TestSubscriber<>();
      dao.deleteTransactionById(id).subscribe(subscriber);
      subscriber.assertNoErrors();
      Assert.assertEquals(1, subscriber.getOnNextEvents().size());
      Assert.assertEquals(1, (int) subscriber.getOnNextEvents().get(0));
      subscriber.unsubscribe();

      // Check if deleted successfully
      TestSubscriber<List<Transaction>> addressSub = new TestSubscriber<>();
      dao.getTransactionForAddress(address).subscribe(addressSub);
      addressSub.assertNoErrors();
      Assert.assertEquals(tests - 1 - i, addressSub.getOnNextEvents().get(0).size());
    }
  }

  /**
   * Checks if two tranasctions have the same values
   *
   * @param a The first transaction
   * @param b The second transaction
   */
  private void checkTransactionEquals(Transaction a, Transaction b) {
    Assert.assertEquals(a.getAddress(), b.getAddress());
    Assert.assertEquals(a.getAmount(), b.getAmount());
    Assert.assertEquals(a.getId(), b.getId());
    Assert.assertEquals(a.getName(), b.getName());
    Assert.assertEquals(a.getSyncState(), b.getSyncState());
    Assert.assertEquals(a.getTimestamp(), b.getTimestamp());
    Assert.assertEquals(a.getTxIndex(), b.getTxIndex());
  }
}
