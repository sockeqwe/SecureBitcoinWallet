package de.tum.in.securebitcoinwallet.addresses;

import de.tum.in.securebitcoinwallet.model.Address;

/**
 * Simple listener that gets invoked once an Address has been clicked
 *
 * @author Hannes Dorfmann
 */
public interface AddressClickListener {

  /**
   * Called when the address has been clicked
   */
  public void onAddressClicked(Address address);
}
