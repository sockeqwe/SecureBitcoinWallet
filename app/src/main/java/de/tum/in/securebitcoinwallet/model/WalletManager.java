package de.tum.in.securebitcoinwallet.model;

import de.tum.in.securebitcoinwallet.model.exception.NotFoundException;
import de.tum.in.securebitcoinwallet.transactions.create.TransactionWizardData;
import java.util.List;
import rx.Observable;

/**
 * This class is responsible to manage addresses and transactions. It's a Facade to prived a single
 * class as etnry point to the business logic
 *
 * @author Hannes Dorfmann
 */
public interface WalletManager {

  /**
   * Get a list of all transaction for a given address
   *
   * @param address the address
   * @return List of transactions
   */
  public Observable<List<Transaction>> getTransactions(String address);

  /**
   * Get a list of all addresses
   *
   * @return List of addresses
   */
  public Observable<List<Address>> getMyAddresses();

  /**
   * Generates and stores a new {@link Address}
   *
   * @param name the name of the address used to display instead of the public key hash
   * @param pin the pin of the secure sd card
   * @return the new generated Address
   */
  public Observable<Address> generateAddress(String name, String pin);

  /**
   * Get the Address details by it's unique address
   *
   * @param address The address
   * @return the address object containing details about this address or a {@link NotFoundException}
   */
  public Observable<Address> getAddress(String address);

  /**
   * Create a Transaction and mark the transaction to be ready to sync
   *
   * @param address The sender address
   * @param data The data about the transaction
   * @return Transaction or exception will be thrown
   */
  public Observable<Transaction> sendTransaction(String pin, String address,
      TransactionWizardData data);

  /**
   * Delete an Address
   *
   * @param address The address to delete
   * @return true if deleted successfully, otherwise exception will be thrown
   */
  public Observable<Boolean> deleteAddress(Address address);

  /**
   * Renames an address
   *
   * @param address The address to rename
   * @param newName The new name
   * @return true if successfull, otherwise exception
   */
  public Observable<Boolean> renameAddress(Address address, String newName);

  public void importWallet();
}
