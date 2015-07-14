package de.tum.in.securebitcoinwallet;

import android.content.Context;
import android.content.Intent;
import de.tum.in.securebitcoinwallet.address.create.CreateAddressActivity;
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
    Intent i = new Intent(context, TransactionsActivity.class);
    i.putExtra(TransactionsActivity.KEY_ADDRESS, address);
    context.startActivity(i);
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
   * @param revealX the revealX position
   * @param revealY ther revealY position
   */
  public void showCreateTransaction(Context context, int revealX, int revealY) {
    Intent i = new Intent(context, CreateTransactionActivity.class);
    i.putExtra(CreateTransactionActivity.KEY_REVEAL_X, revealX);
    i.putExtra(CreateTransactionActivity.KEY_REVEAL_Y, revealY);
    context.startActivity(i);
  }
}
