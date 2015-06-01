package de.tum.in.securebitcoinwallet.model;

import de.tum.in.securebitcoinwallet.model.exception.NotFoundException;
import java.util.List;
import rx.Observable;

/**
 * This class is responsible to manage addresses and transactions
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
   * @return the new generated Address
   */
  public Observable<Address> generateAddress(String name);

  /**
   * Get the Address details by it's unique address
   *
   * @param address The address
   * @return the address object containing details about this address or a {@link NotFoundException}
   */
  public Observable<Address> getAddress(String address);

  public void importWallet();
}
