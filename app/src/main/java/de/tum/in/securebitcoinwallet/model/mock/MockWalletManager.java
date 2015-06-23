package de.tum.in.securebitcoinwallet.model.mock;

import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.PrivateKeyManager;
import de.tum.in.securebitcoinwallet.model.Transaction;
import de.tum.in.securebitcoinwallet.model.WalletManager;
import de.tum.in.securebitcoinwallet.model.exception.NotFoundException;
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
        MockDelayer.delay();
        return Observable.just(database.getTransactions(address));
      }
    });
  }

  @Override public Observable<List<Address>> getMyAddresses() {

    return Observable.defer(new Func0<Observable<List<Address>>>() {
      @Override public Observable<List<Address>> call() {

        MockDelayer.delay();

        return Observable.just(database.getAddresses());
      }
    });
  }

  @Override public Observable<Address> generateAddress(String name) {

    final Address a = new Address();
    a.setAmount(0);
    a.setAddress(database.nextAddressId());
    a.setName(name);

    return Observable.defer(new Func0<Observable<Address>>() {
      @Override public Observable<Address> call() {

        MockDelayer.delay();

        database.addAddress(a);
        return Observable.just(a);
      }
    });
  }

  @Override public Observable<Address> getAddress(final String address) {
    return Observable.defer(new Func0<Observable<Address>>() {
      @Override public Observable<Address> call() {
        MockDelayer.delay();
        Address a = database.getAddress(address);

        if (a == null) {
          return Observable.error(new NotFoundException());
        }

        return Observable.just(a);
      }
    });
  }

  @Override public void importWallet() {

  }
}
