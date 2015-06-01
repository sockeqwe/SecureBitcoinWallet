package de.tum.in.securebitcoinwallet;

import android.content.Context;
import android.content.Intent;
import de.tum.in.securebitcoinwallet.transactions.TransactionsActivity;

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
}
