package de.tum.in.securebitcoinwallet.model;

import android.content.Context;
import com.hannesdorfmann.sqlbrite.dao.DaoManager;
import de.tum.in.securebitcoinwallet.model.database.AddressDao;
import de.tum.in.securebitcoinwallet.model.database.TransactionDao;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Testing {@link BitcoinSync}
 *
 * @author Hannes Dorfmann
 */
@RunWith(RobolectricTestRunner.class) @Config(emulateSdk = 18) public class BitcoinSyncTest {

  private int databaseCounter = 0;
  private BitcoinSync sync;
  private TransactionDao transactionDao;
  private AddressDao addressDao;

  @Before public void init() {
    Context context = Robolectric.getShadowApplication().getApplicationContext();
    transactionDao = new TransactionDao();
    addressDao = new AddressDao();
    new DaoManager(context, "Sync"+(databaseCounter++)+".db", 1, addressDao, transactionDao);
  }
}
