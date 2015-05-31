package de.tum.in.securebitcoinwallet;

import android.app.Activity;
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
   * @param a The activity
   * @param address The address you want to dispaly the transactions for
   */
  public void showTransactions(Activity a, String address) {
    Intent i = new Intent(a, TransactionsActivity.class);
    i.putExtra(TransactionsActivity.KEY_ADDRESS, address);
    a.startActivity(i);
  }
}
