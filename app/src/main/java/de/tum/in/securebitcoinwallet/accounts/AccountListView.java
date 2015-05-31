package de.tum.in.securebitcoinwallet.accounts;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import de.tum.in.securebitcoinwallet.model.Address;
import java.util.List;

/**
 * The AccountListView displays a list of {@link Address}. Gets controlled by {@link
 * AccountListPresenter}
 *
 * @author Hannes Dorfmann
 */
public interface AccountListView extends MvpLceView<List<Address>> {
}
