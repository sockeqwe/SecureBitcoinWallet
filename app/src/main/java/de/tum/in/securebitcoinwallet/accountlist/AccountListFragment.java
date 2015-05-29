package de.tum.in.securebitcoinwallet.accountlist;

import de.tum.in.securebitcoinwallet.common.ListAdapter;
import de.tum.in.securebitcoinwallet.common.RecyclerViewFragment;
import de.tum.in.securebitcoinwallet.model.Address;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class AccountListFragment
    extends RecyclerViewFragment<List<Address>, AccountListView, AccountListPresenter>
    implements AccountListView {

  @Override protected ListAdapter<List<Address>> createAdapter() {
    return new AccountListAdapter(getActivity(), getObjectGraph());
  }

  @Override public AccountListPresenter createPresenter() {
    return getObjectGraph().get(AccountListPresenter.class);
  }

  @Override public void loadData(boolean pullToRefresh) {
    presenter.loadList(pullToRefresh);
  }
}
