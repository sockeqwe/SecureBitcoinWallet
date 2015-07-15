package de.tum.in.securebitcoinwallet.addresses.create.nameinput;

import de.tum.in.securebitcoinwallet.common.view.BitcoinMvpView;

/**
 * The view interface for inserting an address nameEditText
 *
 * @author Hannes Dorfmann
 */
public interface NameInputView extends BitcoinMvpView {

  /**
   * Shows the info that the nameEditText is ok and valid
   */
  public void showAddressNameValid();

  /**
   * Shows an info that the address nameEditText is already in use
   */
  public void showAddressNameAlreadyInUse();
}
