package de.tum.in.securebitcoinwallet.address.create;

import android.os.Bundle;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.common.CircleRevealActivity;

/**
 * The Activity to create a new address
 *
 * @author Hannes Dorfmann
 */
public class CreateAddressActivity extends CircleRevealActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    toolbar.setTitle(R.string.activity_create_address);

    // Activity starting for the first time
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.fragmentContainer, new NameInputFragment())
          .commit();
    }
  }
}
