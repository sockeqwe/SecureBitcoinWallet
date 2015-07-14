package de.tum.in.securebitcoinwallet.transactions.create.transactionpininput;

import de.tum.in.securebitcoinwallet.lock.LockView;
import de.tum.in.securebitcoinwallet.model.Transaction;

/**
 * View MVP interface fot a view that let you insert a pin and generates and submits a new signed
 * transaction.
 *
 * @author Hannes Dorfmann
 */
public interface TransactionPinInputView extends LockView {

  /**
   * Called when Tranasction has been created successfully
   */
  public void showCreatingTransactionSuccessful(Transaction transaction);
}
