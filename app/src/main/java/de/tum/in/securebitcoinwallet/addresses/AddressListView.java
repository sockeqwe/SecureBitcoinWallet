package de.tum.in.securebitcoinwallet.addresses;

import de.tum.in.securebitcoinwallet.common.view.BitcoinMvpLceView;
import de.tum.in.securebitcoinwallet.model.Address;
import java.util.List;

/**
 * The AccountListView displays a list of {@link Address}. Gets controlled by {@link
 * AddressListPresenter}
 *
 * @author Hannes Dorfmann
 */
public interface AddressListView extends BitcoinMvpLceView<List<Address>> {

  /**
   * Shows an error message that the selected address could not be deleted
   *
   * @param address The address to delete
   */
  public void showErrorDeleteingAddress(Throwable t, Address address);

  /**
   * Show s an error message that editing and address has failed
   *
   * @param address The address
   */
  public void showErrorEditingAddress(Throwable t, Address address);
}
