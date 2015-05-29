package de.tum.in.securebitcoinwallet.lock;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * The View interface used with {@link LockPresenter}
 * @author Hannes Dorfmann
 */
public interface LockView extends MvpView {

  /**
   * Shows an error in the UI
   * @param t The error cause
   */
  public void showError(Throwable t);

  /**
   * Shows the input
   */
  public void showInput();

  /**
   * Shows loading
   */
  public void showLoading();

}
