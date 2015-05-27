package de.tum.in.securebitcoinwallet.model;

import rx.Observable;

/**
 * This class is responsible to send bitcoins
 * @author Hannes Dorfmann
 */
public interface TransactionManager {

  /**
   * Send bitcoins from the users address to another address
   * @param myAddress The address of the user
   * @param recipient The address of the recipient
   * @param amount The amount of bitcoins in Satoshi
   * @return Transaction submitted to the bitcoin network
   */
  public Observable<Transaction> sendBitcoins(String myAddress, String recipient, long amount);


}
