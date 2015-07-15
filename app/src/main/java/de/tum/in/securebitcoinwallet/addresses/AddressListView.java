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
}
