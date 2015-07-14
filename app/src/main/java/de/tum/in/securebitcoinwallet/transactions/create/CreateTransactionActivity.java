package de.tum.in.securebitcoinwallet.transactions.create;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.Toast;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.common.CircleRevealActivity;
import de.tum.in.securebitcoinwallet.transactions.create.transactioninput.TransactionInputFragment;
import de.tum.in.securebitcoinwallet.transactions.create.transactioninput.TransactionInputFragmentBuilder;
import de.tum.in.securebitcoinwallet.transactions.create.transactionpininput.TransactionPinInputFragment;
import de.tum.in.securebitcoinwallet.transactions.create.transactionpininput.TransactionPinInputFragmentBuilder;
import icepick.Icicle;

/**
 * The Activity to create a new address
 *
 * @author Hannes Dorfmann
 */
public class CreateTransactionActivity extends CircleRevealActivity
    implements TransactionInputFragment.TransactionInputListner {

  public static final String KEY_SENDER_ADDRESS =
      "de.tum.in.securebitcoinwallet.transaction.create.SENDER_ADDRESS";

  private final String FRAGMENT_TAG = "de.tum.in.securebitcoinwallet.transaction.create.FRAGMENT";

  private Fragment currentFragment;

  @Icicle TransactionWizardData transactionWizardData = null;
  private String senderAddress;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    senderAddress = getIntent().getStringExtra(KEY_SENDER_ADDRESS);
    if (TextUtils.isEmpty(senderAddress)) {
      Toast.makeText(this, R.string.error_create_transaction_sender_no_address, Toast.LENGTH_SHORT)
          .show();
      finish();
      return;
    }

    toolbar.setTitle(R.string.activity_create_transaction);

    // Activity starting for the first time
    if (savedInstanceState == null) {
      showTransactionInput(false);
    } else {
      currentFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }
  }

  /**
   * Shows the wizard data input fragment
   */
  private void showTransactionInput(boolean animated) {

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

    if (animated) {
      transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
    }
    currentFragment =
        new TransactionInputFragmentBuilder().wizardData(transactionWizardData).build();
    transaction.replace(R.id.fragmentContainer, currentFragment, FRAGMENT_TAG).commit();
  }

  /**
   * Displays the pin InputFragment instead of any other previous fragment
   */
  private void showPinInputFragment() {

    if (transactionWizardData == null) {
      showTransactionInput(false); // Unexpected error, should never be the case
    } else {
      currentFragment =
          new TransactionPinInputFragmentBuilder(senderAddress, transactionWizardData).build();
      getSupportFragmentManager().beginTransaction()
          .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
          .replace(R.id.fragmentContainer, currentFragment, FRAGMENT_TAG)
          .commit();
    }
  }

  @Override public void onTransactionWizardFilled(TransactionWizardData wizardData) {
    this.transactionWizardData = wizardData;
    showPinInputFragment();
  }

  @Override public void onBackPressed() {
    if (currentFragment instanceof TransactionPinInputFragment) {
      showTransactionInput(true);
    } else {
      super.onBackPressed();
    }
  }
}
