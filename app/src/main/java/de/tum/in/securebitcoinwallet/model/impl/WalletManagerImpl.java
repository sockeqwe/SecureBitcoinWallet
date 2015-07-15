package de.tum.in.securebitcoinwallet.model.impl;

import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.PrivateKeyManager;
import de.tum.in.securebitcoinwallet.model.Transaction;
import de.tum.in.securebitcoinwallet.model.WalletManager;
import de.tum.in.securebitcoinwallet.model.database.AddressDao;
import de.tum.in.securebitcoinwallet.model.database.TransactionDao;
import de.tum.in.securebitcoinwallet.transactions.create.TransactionWizardData;
import de.tum.in.securebitcoinwallet.util.BitcoinUtils;
import java.util.List;
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
    return privateKeyManager.generateNewKey(convertPinToBytes(pin))
        .flatMap(new Func1<byte[], Observable<Address>>() {
          @Override public Observable<Address> call(byte[] bytes) {
            Address a = new Address();
            a.setAddress(BitcoinUtils.calculateBitcoinAddress(bytes));
            a.setName(name);
            a.setPublicKey(bytes);

            return addressDao.insertOrUpdateAddress(a);
          }
        });
  }

  @Override public Observable<Address> getAddress(String address) {
    return addressDao.getAddress(address, false);
  }

  @Override public Observable<Transaction> sendTransaction(String pin, final String address,
      final TransactionWizardData data) {
    byte[] transactionData = { 1, 0, 1 }; // TODO implement
    return privateKeyManager.signSHA256Hash(convertPinToBytes(pin), address, transactionData)
        .flatMap(new Func1<byte[], Observable<Transaction>>() {
          @Override public Observable<Transaction> call(byte[] signedTransaction) {
            Transaction t = new Transaction();
            t.setAmount(data.getSatoshi());
            t.setName(data.getReference());
            t.setSyncState(Transaction.SYNC_NOT_SUBMITTED);
            t.setAddress(address);
            t.setId(BitcoinUtils.getHashFromSignedTransaction(signedTransaction));
            t.setTimestamp(System.currentTimeMillis());

            return transactionDao.insertOrUpdate(t);
          }
        });
  }

  @Override public void importWallet() {

    // TODO to specify

  }

  /**
   * Converts the given pin string to a byte array suitable for use with the PrivateKeyManager
   *
   * @param pin The pin as a String of numbers
   * @return The pin as a byte array
   */
  private byte[] convertPinToBytes(String pin) {
    byte[] convertedPin = new byte[pin.length()];
    for (int i = 0; i < pin.length(); i++) {
      convertedPin[i] = Byte.parseByte(pin.substring(i, i + 1));
    }
    return convertedPin;
  }

  @Override public Observable<Boolean> deleteAddress(final Address address) {

    return privateKeyManager.deleteAddress(address.getAddress())
        .flatMap(new Func1<Boolean, Observable<Integer>>() {
          @Override public Observable<Integer> call(Boolean aBoolean) {
            return addressDao.delete(address);
          }
        })
        .flatMap(new Func1<Integer, Observable<Integer>>() {
          @Override public Observable<Integer> call(Integer integer) {
            return transactionDao.deleteTransactionForAddress(address.getAddress());
          }
        })
        .map(new Func1<Integer, Boolean>() {
          @Override public Boolean call(Integer integer) {
            return true;
          }
        });
  }
}
