package de.tum.in.securebitcoinwallet.address.create.nameinput;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * The view interface for inserting an address name
 *
 * @author Hannes Dorfmann
 */
public interface NameInputView extends MvpView {

  /**
   * Shows the info that the name is ok and valid
   */
  public void showAddressNameValid();

  /**
   * Shows an info that the address name is already in use
   */
  public void showAddressNameAlreadyInUse();
}
