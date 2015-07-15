package de.tum.in.securebitcoinwallet.addresses;

import de.tum.in.securebitcoinwallet.model.Address;

/**
 * A simple listener that gets notified when an address has been long clicked
 *
 * @author Hannes Dorfmann
 */
public interface AddressLongClickListener {

  /**
   * Called when the address has been long clicked
   *
   * @param address The long clicked address
   */
  public void onAddressLongClicked(Address address);
}
