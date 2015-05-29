package de.tum.in.securebitcoinwallet.lock;

import com.hannesdorfmann.mosby.mvp.rx.MvpRxPresenter;
import de.greenrobot.event.EventBus;
import de.tum.in.securebitcoinwallet.model.PrivateKeyManager;
import javax.inject.Inject;

/**
 * The presenter for the {@link LockView}
 *
 * @author Hannes Dorfmann
 */
public class LockPresenter extends MvpRxPresenter<LockView, Void> {

  private PrivateKeyManager keyManager;
  private EventBus eventBus;

  @Inject public LockPresenter(PrivateKeyManager keyManager, EventBus eventBus) {
    this.keyManager = keyManager;
    this.eventBus = eventBus;
  }

  /**
   * Checks if the pin is correct. Will fire a {@link UnlockEvent} if PIN was correct.
   *
   * @param pin The pin you want to check
   */
  public void checkPin(String pin) {
    if (isViewAttached()) {
      getView().showLoading();
    }
    subscribe(keyManager.checkPin(pin));
  }

  @Override protected void onNext(Void aVoid) {
  }

  @Override protected void onError(Throwable throwable) {
    if (isViewAttached()) {
      getView().showError(throwable);
    }
  }

  @Override protected void onCompleted() {
    eventBus.post(new UnlockEvent());
  }
}
