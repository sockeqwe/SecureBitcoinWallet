package de.tum.in.securebitcoinwallet;

import android.content.Context;
import android.content.Intent;
import de.tum.in.securebitcoinwallet.addresses.create.CreateAddressActivity;
import de.tum.in.securebitcoinwallet.transactions.TransactionsActivity;
import de.tum.in.securebitcoinwallet.transactions.create.CreateTransactionActivity;

/**
 * A simple class responsible to navigate from one activity to another.
 *
 * @author Hannes Dorfmann
 */
public class IntentStarter {

  /**
   * Navigate to the list of transaction
   *
   * @param context The context
   * @param address The address you want to dispaly the transactions for
   */
  public void showTransactions(Context context, String address) {
    context.startActivity(getIntentToShowTransactions(context, address));
  }

  /**
   * Get the intent to display the transactions list for a certain address
   *
   * @param context The who starts this
   * @param address The address
   */
  public Intent getIntentToShowTransactions(Context context, String address) {
    Intent i = new Intent(context, TransactionsActivity.class);
    i.putExtra(TransactionsActivity.KEY_ADDRESS, address);
    return i;
  }

  /**
   * Starts the activity to create a new address
   *
   * @param context the context
   * @param revealX the revealX position
   * @param revealY the revealY position
   */
  public void showCreateAddress(Context context, int revealX, int revealY) {
    Intent i = new Intent(context, CreateAddressActivity.class);
    i.putExtra(CreateAddressActivity.KEY_REVEAL_X, revealX);
    i.putExtra(CreateAddressActivity.KEY_REVEAL_Y, revealY);
    context.startActivity(i);
  }

  /**
   * Stats the activity to create a new Transcation (send Bitcoins)
   *
   * @param context The context
   * @param senderAddress The address from which the bicoins will be sends
   * @param revealX the revealX position
   * @param revealY ther revealY position
   */
  public void showCreateTransaction(Context context, String senderAddress, int revealX,
      int revealY) {
    Intent i = new Intent(context, CreateTransactionActivity.class);
    i.putExtra(CreateTransactionActivity.KEY_REVEAL_X, revealX);
    i.putExtra(CreateTransactionActivity.KEY_REVEAL_Y, revealY);
    i.putExtra(CreateTransactionActivity.KEY_SENDER_ADDRESS, senderAddress);
    context.startActivity(i);
  }
}
