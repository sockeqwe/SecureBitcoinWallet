package de.tum.in.securebitcoinwallet.model.impl;

import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.PrivateKeyManager;
import de.tum.in.securebitcoinwallet.model.Transaction;
import de.tum.in.securebitcoinwallet.model.WalletManager;
import de.tum.in.securebitcoinwallet.model.database.AddressDao;
import de.tum.in.securebitcoinwallet.model.database.TransactionDao;
import de.tum.in.securebitcoinwallet.util.BitcoinUtils;
import java.util.List;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import rx.Observable;
import rx.functions.Func1;

/**
 * Faceade implementation of {@link WalletManager}
 *
 * @author Hannes Dorfmann
 */
public class WalletManagerImpl implements WalletManager {

  private PrivateKeyManager privateKeyManager;
  private AddressDao addressDao;
  private TransactionDao transactionDao;

  public WalletManagerImpl(PrivateKeyManager privateKeyManager, AddressDao addressDao,
      TransactionDao transactionDao) {
    this.privateKeyManager = privateKeyManager;
    this.addressDao = addressDao;
    this.transactionDao = transactionDao;
  }

  @Override public Observable<List<Transaction>> getTransactions(String address) {
    return transactionDao.getTransactionForAddress(address);
  }

  @Override public Observable<List<Address>> getMyAddresses() {
    return addressDao.getAddresses();
  }

  @Override public Observable<Address> generateAddress(final String name, String pin) {
    return privateKeyManager.generateNewKey(pin.getBytes())
        .flatMap(new Func1<ECPublicKey, Observable<Address>>() {
          @Override public Observable<Address> call(ECPublicKey ecPublicKey) {

            Address a = new Address();
            a.setAddress(BitcoinUtils.calculateBitcoinAddress(ecPublicKey));
            a.setName(name);

            return addressDao.insertOrUpdateAddress(a);
          }
        });
  }

  @Override public Observable<Address> getAddress(String address) {
    return addressDao.getAddress(address, false);
  }

  @Override public void importWallet() {

    // TODO to specify

  }
}