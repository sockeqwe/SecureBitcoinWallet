package de.tum.in.securebitcoinwallet.model.mock;

import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.PrivateKeyManager;
import de.tum.in.securebitcoinwallet.model.Transaction;
import de.tum.in.securebitcoinwallet.model.WalletManager;
import java.util.List;
import rx.Observable;

/**
 * @author Hannes Dorfmann
 */
public class MockWalletManager implements WalletManager {

  private PrivateKeyManager privateKeyManager;

  public MockWalletManager(PrivateKeyManager privateKeyManager) {
    this.privateKeyManager = privateKeyManager;
  }

  @Override public Observable<List<Transaction>> getTransactions(String address) {
    return null;
  }

  @Override public Observable<List<Address>> getMyAddresses() {
    return null;
  }

  @Override public Observable<Address> generateAddress() {
    return null;
  }

  @Override public void importWallet() {

  }
}
