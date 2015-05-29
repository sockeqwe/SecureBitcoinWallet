package de.tum.in.securebitcoinwallet.lock;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

/**
 * @author Hannes Dorfmann
 */
public class LockViewState implements ViewState<LockView> {

  private final int STATE_SHOWING_INPUT = 0;
  private final int STATE_SHOWING_ERROR = 1;
  private final int STATE_SHOWING_LOADING = 2;

  private int state;
  private Throwable errorCause;

  public void setShowingInput() {
    state = STATE_SHOWING_INPUT;
    errorCause = null;
  }

  public void setShowingLoading() {
    state = STATE_SHOWING_LOADING;
    errorCause = null;
  }

  public void setShowingError(Throwable error) {
    state = STATE_SHOWING_ERROR;
    this.errorCause = error;
  }

  @Override public void apply(LockView lockView, boolean b) {
    if (state == STATE_SHOWING_INPUT) {
      lockView.showInput();
    } else if (state == STATE_SHOWING_LOADING) {
      lockView.showLoading();
    } else if (state == STATE_SHOWING_ERROR) {
      lockView.showError(errorCause);
      lockView.showInput();
    }
  }
}
