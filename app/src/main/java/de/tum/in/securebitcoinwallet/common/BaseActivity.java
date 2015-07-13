package de.tum.in.securebitcoinwallet.common;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.hannesdorfmann.mosby.dagger1.Dagger1MosbyActivity;
import de.greenrobot.event.EventBus;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.lock.LockEvent;
import de.tum.in.securebitcoinwallet.lock.LockFragment;
import de.tum.in.securebitcoinwallet.lock.UnlockEvent;
import javax.inject.Inject;

/**
 * @author Hannes Dorfmann
 */
public abstract class BaseActivity extends Dagger1MosbyActivity {

  private static long lastUnlock = 0;
  private final String FRAGMENT_TAG = "de.tum.in.securebitcoinwallet.FRAGMENT_TAG";

  @Inject EventBus eventBus;

  private Fragment currentFragment;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    eventBus.register(this);

    currentFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    eventBus.unregister(this);
  }

  @Override protected void onResume() {
    super.onResume();
    if (isLocked()) {
      showLockFragment();
    } else {
      showContentFragment();
    }
  }

  /**
   * Checks whether the lockscreen should be shown.
   * @return True, if the screen should be locked, false otherwise
   */
  private boolean isLocked() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    if (!prefs.getBoolean("pref_lock", true)) {
      return false;
    }
    // Using getString because getInteger throws parse error
    long lockAfter = Integer.parseInt(prefs.getString("pref_lock_timeout", "60"));
    lockAfter *= 1000;
    long diff = System.currentTimeMillis() - lastUnlock;
    return diff > lockAfter;
  }

  public void onEventMainThread(UnlockEvent event) {
    showContentFragment();
  }

  public void onEventMainThread(LockEvent event) {
    showLockFragment();
  }

  private void showLockFragment() {
    if (currentFragment == null || !(currentFragment instanceof LockFragment)) {
      currentFragment = new LockFragment();
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.fragmentContainer, currentFragment, FRAGMENT_TAG)
          .commit();
    }
  }

  private void showContentFragment() {

    if (currentFragment == null || currentFragment instanceof LockFragment) {
      Fragment contentFragment = getContentFragment();
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      if (currentFragment != null) {
        transaction.setCustomAnimations(0, R.anim.zoom_out);
      }
      transaction.replace(R.id.fragmentContainer, contentFragment, FRAGMENT_TAG).commit();

      currentFragment = contentFragment;

      lastUnlock = System.currentTimeMillis();
    }
  }

  protected abstract Fragment getContentFragment();
}
