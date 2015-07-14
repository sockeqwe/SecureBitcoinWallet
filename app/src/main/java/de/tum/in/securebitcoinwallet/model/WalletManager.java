package de.tum.in.securebitcoinwallet.model;

import de.tum.in.securebitcoinwallet.model.exception.NotFoundException;
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
   *
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

  public void importWallet();
}
