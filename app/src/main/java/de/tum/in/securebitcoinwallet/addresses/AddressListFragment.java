package de.tum.in.securebitcoinwallet.addresses;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import butterknife.InjectView;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import de.tum.in.securebitcoinwallet.IntentStarter;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.common.ListAdapter;
import de.tum.in.securebitcoinwallet.common.RecyclerViewFragment;
import de.tum.in.securebitcoinwallet.preferences.SettingsActivity;
import de.tum.in.securebitcoinwallet.model.Address;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Hannes Dorfmann
 */
public class AddressListFragment
    extends RecyclerViewFragment<List<Address>, AddressListView, AddressListPresenter>
    implements AddressListView {

  @InjectView(R.id.toolbar) Toolbar toolbar;
  @Inject IntentStarter intentStarter;

  private RFACLabelItem fabNew;
  private RFACLabelItem fabImport;

  @Override protected ListAdapter<List<Address>> createAdapter() {
    return new AddressListAdapter(getActivity(), getObjectGraph());
  }

  @Override public AddressListPresenter createPresenter() {
    return getObjectGraph().get(AddressListPresenter.class);
  }

  @Override public void loadData(boolean pullToRefresh) {
    presenter.loadList();
  }

  @Override protected List<RFACLabelItem> getFabMenuItems() {

    Resources res = getResources();
    Drawable background = res.getDrawable(R.drawable.fab_menu_label);

    List<RFACLabelItem> items = new ArrayList<>(3);

    fabNew = new RFACLabelItem<Integer>().setLabel(getString(R.string.menu_address_new))
        .setResId(R.drawable.ic_key)
        .setIconNormalColor(res.getColor(R.color.menu_icon2))
        .setIconPressedColor(res.getColor(R.color.menu_icon2_pressed))
        .setLabelColor(res.getColor(R.color.menu_label_text_color))
        .setLabelBackgroundDrawable(background)
        .setWrapper(3);

    fabImport = new RFACLabelItem<Integer>().setLabel(getString(R.string.menu_address_import))
        .setResId(R.drawable.ic_cards)
        .setIconNormalColor(res.getColor(R.color.menu_icon1))
        .setIconPressedColor(res.getColor(R.color.menu_icon1_pressed))
        .setLabelColor(res.getColor(R.color.menu_label_text_color))
        .setLabelBackgroundDrawable(background)
        .setWrapper(3);

    items.add(fabNew);
    items.add(fabImport);

    return items;
  }

  @Override protected void onFabMenuItemClicked(int postion, RFACLabelItem item, View view) {

    int location[] = new int[2];
    view.getLocationInWindow(location);
    if (item == fabNew) {
      intentStarter.showCreateAddress(getActivity(), location[0], location[1]);
    } else {

    }
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    toolbar.setTitle(R.string.app_name);
    toolbar.inflateMenu(R.menu.menu_account_list);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
          Intent settings = new Intent(getActivity(), SettingsActivity.class);
          startActivity(settings);
          return true;
        }
        return false;
      }
    });
  }

  @Override public boolean isChangingConfigurations() {
    return getActivity() != null && getActivity().isChangingConfigurations();
  }
}