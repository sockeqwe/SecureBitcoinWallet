package de.tum.in.securebitcoinwallet.model.mock;

import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.PrivateKeyManager;
import de.tum.in.securebitcoinwallet.model.Transaction;
import de.tum.in.securebitcoinwallet.model.WalletManager;
import java.util.List;
import rx.Observable;
import rx.functions.Func0;

/**
 * @author Hannes Dorfmann
 */
public class MockWalletManager implements WalletManager {

  private PrivateKeyManager privateKeyManager;
  private MockDatabase database = new MockDatabase();

  public MockWalletManager(PrivateKeyManager privateKeyManager) {
    this.privateKeyManager = privateKeyManager;
  }

  @Override public Observable<List<Transaction>> getTransactions(final String address) {

    return Observable.defer(new Func0<Observable<List<Transaction>>>() {
      @Override public Observable<List<Transaction>> call() {
        return Observable.just(database.getTransactions(address));
      }
    });
  }

  @Override public Observable<List<Address>> getMyAddresses() {

    return Observable.defer(new Func0<Observable<List<Address>>>() {
      @Override public Observable<List<Address>> call() {
        return Observable.just(database.getAddresses());
      }
    });
  }

  @Override public Observable<Address> generateAddress(String name) {

    final Address a = new Address();
    a.setAmount(0);
    a.setAddress(database.nextAddressId());
    a.setPublicKey("PublicKey-" + a.getAddress());
    a.setName(name);

    return Observable.defer(new Func0<Observable<Address>>() {
      @Override public Observable<Address> call() {

        database.addAddress(a);
        return Observable.just(a);
      }
    });
  }

  @Override public void importWallet() {

  }
}
