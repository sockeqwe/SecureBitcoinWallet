package de.tum.in.securebitcoinwallet.common;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Base View for every MVP View in this apps
 *
 * @author Hannes Dorfmann
 */
public interface BitcoinMvpView extends MvpView {

  /**
   * Return true if the view gets destroyed because of a screen orientation change
   *
   * @return true, if caused by screen orientation changes, otherwise false
   */
  public boolean isChangingConfigurations();
}
