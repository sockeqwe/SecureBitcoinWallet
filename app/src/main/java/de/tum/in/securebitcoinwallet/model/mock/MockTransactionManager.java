package de.tum.in.securebitcoinwallet.model.mock;

import de.tum.in.securebitcoinwallet.model.Transaction;
import de.tum.in.securebitcoinwallet.model.TransactionManager;
import rx.Observable;

/**
 * @author Hannes Dorfmann
 */
public class MockTransactionManager implements TransactionManager {
  @Override
  public Observable<Transaction> sendBitcoins(String myAddress, String recipient, long amount) {
    return null;
  }
}
