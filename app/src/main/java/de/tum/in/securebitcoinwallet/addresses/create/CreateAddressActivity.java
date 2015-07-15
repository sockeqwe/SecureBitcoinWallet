package de.tum.in.securebitcoinwallet.addresses.create;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.addresses.create.nameinput.NameInputFragment;
import de.tum.in.securebitcoinwallet.addresses.create.nameinput.NameInputFragmentBuilder;
import de.tum.in.securebitcoinwallet.addresses.create.pininput.PinInputFragment;
import de.tum.in.securebitcoinwallet.addresses.create.pininput.PinInputFragmentBuilder;
import de.tum.in.securebitcoinwallet.common.CircleRevealActivity;
import icepick.Icicle;

/**
 * The Activity to create a new address
 *
 * @author Hannes Dorfmann
 */
public class CreateAddressActivity extends CircleRevealActivity
    implements NameInputFragment.NameInputListner {

  private final String FRAGMENT_TAG = "de.tum.in.securebitcoinwallet.address.create.FRAGMENT";

  private Fragment currentFragment;

  @Icicle String addressName = null;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    toolbar.setTitle(R.string.activity_create_address);

    // Activity starting for the first time
    if (savedInstanceState == null) {
      showNameInputFragment(false);
    } else {
      currentFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }
  }

  /**
   * Shows the NameInputFragment
   */
  private void showNameInputFragment(boolean animated) {

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

    if (animated) {
      transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    currentFragment = new NameInputFragmentBuilder().name(addressName).build();
    transaction.replace(R.id.fragmentContainer, currentFragment, FRAGMENT_TAG).commit();
  }

  /**
   * Displays the showInputFragment instead of any other previous fragment
   */
  private void showPinInputFragment() {

    if (addressName == null) {
      showNameInputFragment(false); // Unexpected error, should never be the case
    } else {
      currentFragment = new PinInputFragmentBuilder(addressName).build();
      getSupportFragmentManager().beginTransaction()
          .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
          .replace(R.id.fragmentContainer, currentFragment, FRAGMENT_TAG)
          .commit();
    }
  }

  @Override public void validAddressNameInserted(String name) {
    this.addressName = name;

    // show PIN fragment
    showPinInputFragment();
  }

  @Override public void onBackPressed() {
    if (currentFragment instanceof PinInputFragment) {
      showNameInputFragment(true);
    } else {
      super.onBackPressed();
    }
  }
}
