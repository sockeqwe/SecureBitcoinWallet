package de.tum.in.securebitcoinwallet.transactions;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import de.tum.in.securebitcoinwallet.common.ListAdapter;
import de.tum.in.securebitcoinwallet.common.RecyclerViewFragment;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class TransactionsFragment extends RecyclerViewFragment {

  @Arg String address;

  @Override protected ListAdapter createAdapter() {
    return null;
  }

  @Override protected List<RFACLabelItem> getFabMenuItems() {
    return null;
  }

  @Override protected void onFabMeuItemClicked(int postion, RFACLabelItem item) {

  }

  @Override public MvpPresenter createPresenter() {
    return null;
  }

  @Override public void setData(Object data) {

  }

  @Override public void loadData(boolean pullToRefresh) {

  }
}
