package de.tum.in.securebitcoinwallet.transactions;

import android.support.v4.app.Fragment;
import de.tum.in.securebitcoinwallet.common.BaseActivity;

/**
 * @author Hannes Dorfmann
 */
public class TransactionsActivity extends BaseActivity {

  public static final String KEY_ADDRESS = "de.tum.in.securebitcoinwallet.transactions.ADDRESS";

  @Override protected Fragment getContentFragment() {
    return new TransactionsFragmentBuilder(getIntent().getStringExtra(KEY_ADDRESS)).build();
  }
}
