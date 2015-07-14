package de.tum.in.securebitcoinwallet.transactions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.common.BaseActivity;

/**
 * @author Hannes Dorfmann
 */
public class TransactionsActivity extends BaseActivity {

  public static final String KEY_ADDRESS = "de.tum.in.securebitcoinwallet.transactions.ADDRESS";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fragement_container);
  }

  @Override protected Fragment getContentFragment() {
    return new TransactionsFragmentBuilder(getIntent().getStringExtra(KEY_ADDRESS)).build();
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(R.anim.swipeback_stack_to_front, R.anim.swipeback_stack_right_out);
  }
}
