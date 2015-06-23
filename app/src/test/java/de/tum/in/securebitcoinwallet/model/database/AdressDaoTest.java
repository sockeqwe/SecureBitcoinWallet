package de.tum.in.securebitcoinwallet.model.database;

import android.content.Context;
import com.hannesdorfmann.sqlbrite.dao.DaoManager;
import de.tum.in.securebitcoinwallet.model.Address;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import rx.Subscription;
import rx.functions.Action1;

/**
 * @author Hannes Dorfmann
 */
@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
public class AdressDaoTest {

  @Test public void insertAddress() {

    Context context = Robolectric.getShadowApplication().getApplicationContext();
    AddressDao dao = new AddressDao();
    new DaoManager(context, "InsertAddressTest", 1, dao);

    final String address = "address1";
    final String name = "name1";
    final long sent = 123;
    final long received = 456;
    final long amount = 987;

    final Address a = new Address();
    a.setName(name);
    a.setAmount(amount);
    a.setAddress(address);
    a.setTotalReceived(received);
    a.setTotalSent(sent);

    dao.insertOrUpdateAddress(a).subscribe(new Action1<Address>() {
      @Override public void call(Address address) {
        Assert.assertTrue(a == address);
      }
    });

    Subscription sub = dao.getAddresses().subscribe(new Action1<List<Address>>() {
      @Override public void call(List<Address> addresses) {
        Assert.assertEquals(1, addresses.size());
        Address queried = addresses.get(0);

        Assert.assertEquals(name, queried.getName());
        Assert.assertEquals(address, queried.getAddress());
        Assert.assertEquals(amount, queried.getAmount());
        Assert.assertEquals(sent, queried.getTotalSent());
        Assert.assertEquals(received, queried.getTotalReceived());
      }
    });

    sub.unsubscribe();


    // Insert updated

    final String name2 = "name1";
    final long sent2 = 222;
    final long received2 = 333;
    final long amount2 = 444;

    final Address a2 = new Address();
    a2.setAddress(address);
    a2.setName(name2);
    a2.setAmount(amount2);
    a2.setTotalReceived(received2);
    a2.setTotalSent(sent2);

    dao.insertOrUpdateAddress(a2).subscribe(new Action1<Address>() {
      @Override public void call(Address address) {
        Assert.assertTrue(a2 == address);
      }
    });

   sub = dao.getAddresses().subscribe(new Action1<List<Address>>() {
      @Override public void call(List<Address> addresses) {
        Assert.assertEquals(1, addresses.size());
        Address queried = addresses.get(0);

        Assert.assertEquals(name2, queried.getName());
        Assert.assertEquals(address, queried.getAddress());
        Assert.assertEquals(amount2, queried.getAmount());
        Assert.assertEquals(sent2, queried.getTotalSent());
        Assert.assertEquals(received2, queried.getTotalReceived());
      }
    });

    sub.unsubscribe();
  }
}
