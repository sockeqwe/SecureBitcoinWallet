package de.tum.in.securebitcoinwallet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.hannesdorfmann.mosby.dagger1.Dagger1MosbyActivity;
import de.greenrobot.event.EventBus;
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

  private boolean isLocked() {
    // TODO implement check lock with shared preferences?
    long lockAfter = 20 * 1000;
    long diff = System.currentTimeMillis() - lastUnlock;
    return  diff > lockAfter;
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
      currentFragment = getContentFragment();
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.fragmentContainer, currentFragment, FRAGMENT_TAG)
          .commit();


      lastUnlock = System.currentTimeMillis();
    }

  }

  protected abstract Fragment getContentFragment();
}
