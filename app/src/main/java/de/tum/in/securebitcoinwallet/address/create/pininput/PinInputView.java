package de.tum.in.securebitcoinwallet.address.create.pininput;

import de.tum.in.securebitcoinwallet.lock.LockView;
import de.tum.in.securebitcoinwallet.model.Address;

/**
 * View MVP interface fot a view that let you insert a pin and generates a new Address.
 *
 * @author Hannes Dorfmann
 */
public interface PinInputView extends LockView {

  /**
   * Called when Address has been created successfully
   * @param address
   */
  public void showCreateAddressSuccessful(Address address);
}
