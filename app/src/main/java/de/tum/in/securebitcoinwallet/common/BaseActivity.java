package de.tum.in.securebitcoinwallet.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.hannesdorfmann.mosby.dagger1.Dagger1MosbyActivity;
import de.tum.in.securebitcoinwallet.R;

/**
 * @author Hannes Dorfmann
 */
public abstract class BaseActivity extends Dagger1MosbyActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.fragmentContainer, getContentFragment())
          .commit();
    }
  }

  /**
   * Get the fragment that gets displayed by this activity
   */
  protected abstract Fragment getContentFragment();
}
