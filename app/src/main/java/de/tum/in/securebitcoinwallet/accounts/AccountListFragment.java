package de.tum.in.securebitcoinwallet.accounts;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.common.ListAdapter;
import de.tum.in.securebitcoinwallet.common.RecyclerViewFragment;
import de.tum.in.securebitcoinwallet.model.Address;
import java.util.ArrayList;
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

  @Override protected List<RFACLabelItem> getFabMenuItems() {

    Resources res = getResources();
    Drawable background = res.getDrawable(R.drawable.fab_menu_label);

    List<RFACLabelItem> items = new ArrayList<>(3);

    items.add(new RFACLabelItem<Integer>().setLabel(getString(R.string.menu_address_new))
        .setResId(R.drawable.ic_key)
        .setIconNormalColor(res.getColor(R.color.menu_icon2))
        .setIconPressedColor(res.getColor(R.color.menu_icon2_pressed))
        .setLabelColor(res.getColor(R.color.menu_label_text_color))
        .setLabelBackgroundDrawable(background)
        .setWrapper(3));

    items.add(new RFACLabelItem<Integer>().setLabel(getString(R.string.menu_address_import))
        .setResId(R.drawable.ic_cards)
        .setIconNormalColor(res.getColor(R.color.menu_icon1))
        .setIconPressedColor(res.getColor(R.color.menu_icon1_pressed))
        .setLabelColor(res.getColor(R.color.menu_label_text_color))
        .setLabelBackgroundDrawable(background)
        .setWrapper(3));


    return items;
  }

  @Override protected void onFabMeuItemClicked(int postion, RFACLabelItem item) {

  }
}
