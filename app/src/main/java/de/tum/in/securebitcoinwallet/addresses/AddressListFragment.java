package de.tum.in.securebitcoinwallet.addresses;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.InjectView;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import de.tum.in.securebitcoinwallet.IntentStarter;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.common.ErrorMessageDeterminer;
import de.tum.in.securebitcoinwallet.common.ListAdapter;
import de.tum.in.securebitcoinwallet.common.RecyclerViewFragment;
import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.preferences.SettingsActivity;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Fragment displaying a list of fragemtns. Furthermore you can delete or edit an address
 *
 * @author Hannes Dorfmann
 */
public class AddressListFragment
    extends RecyclerViewFragment<List<Address>, AddressListView, AddressListPresenter>
    implements AddressListView, AddressLongClickListener, AddressClickListener {

  private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
    @Override public boolean onCreateActionMode(ActionMode mode, Menu menu) {

      MenuInflater inflater = mode.getMenuInflater();
      inflater.inflate(R.menu.actionmode_addresslist, menu);
      return true;
    }

    @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
      return false;
    }

    @Override public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
      if (item.getItemId() == R.id.actionModeDelete) {
        deleteAddress();
        return true;
      }

      if (item.getItemId() == R.id.actionModeEdit) {
        editAddress();
        return true;
      }
      return false;
    }

    @Override public void onDestroyActionMode(ActionMode mode) {
      actionMode = null;
      longClickedAddress = null;
      ((AddressListAdapter) adapter).setSelectedItem(null);
      adapter.notifyDataSetChanged();
    }
  };

  @InjectView(R.id.toolbar) Toolbar toolbar;
  @Inject IntentStarter intentStarter;
  @Inject ErrorMessageDeterminer errorMessageDeterminer;

  private RFACLabelItem fabNew;
  private RFACLabelItem fabImport;
  private ActionMode actionMode;
  private Address longClickedAddress;

  @Override protected ListAdapter<List<Address>> createAdapter() {
    return new AddressListAdapter(getActivity(), getObjectGraph(), this, this);
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
      // TODO implement
    }
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    toolbar.setTitle(R.string.app_name);
    //toolbar.inflateMenu(R.menu.menu_account_list);
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

  @Override public void onAddressLongClicked(Address address) {
    if (actionMode == null) {
      actionMode = getActivity().startActionMode(actionModeCallback);
    }

    setSelectedItem(address);
  }

  @Override public void onAddressClicked(Address address) {
    if (actionMode == null) {
      intentStarter.showTransactions(getActivity(), address.getAddress());
    } else {
      setSelectedItem(address);
    }
  }

  /**
   * Marks a certain item as selected
   */
  private void setSelectedItem(Address address) {
    longClickedAddress = address;
    ((AddressListAdapter) adapter).setSelectedItem(address);
    adapter.notifyDataSetChanged();
  }

  /**
   * Displays an dialog to delete an address
   */
  private void deleteAddress() {

    View rootView = getActivity().getLayoutInflater().inflate(R.layout.view_passwordinput, null);
    final EditText pinEditText = (EditText) rootView.findViewById(R.id.editText);
    pinEditText.requestFocus();

    new AlertDialog.Builder(getActivity()).setTitle(R.string.actionmode_delete_address_title)
        .setMessage(R.string.actionmode_delete_address_message)
        .setView(rootView)
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            String pin = pinEditText.getText().toString();
            presenter.deleteAddress(pin, longClickedAddress);
            actionMode.finish();
          }
        })
        .show();
  }

  /**
   * Displays an Dialog to edit the address name
   */
  private void editAddress() {

    View rootView = getActivity().getLayoutInflater().inflate(R.layout.view_textinput, null);
    final EditText nameEditText = (EditText) rootView.findViewById(R.id.editText);
    nameEditText.setText(longClickedAddress.getName());

    new AlertDialog.Builder(getActivity()).setTitle(R.string.actionmode_edit_address_title)
        .setView(rootView)
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            String newName = nameEditText.getText().toString();
            if (TextUtils.isEmpty(newName)) {
              showSnackbar(getResources().getString(R.string.actionmode_edit_address_empty_name));
            } else {
              presenter.renameAddress(longClickedAddress, newName);
              actionMode.finish();
            }
          }
        })
        .show();
  }

  /**
   * Shows a snackbar dispalying an error message
   */
  private void showSnackbar(String msg) {
    Snackbar snackbar = Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG);
    TextView tv =
        (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
    tv.setTextColor(Color.WHITE);

    snackbar.show();
  }

  @Override public void showErrorDeleteingAddress(Throwable t, Address address) {
    showSnackbar(errorMessageDeterminer.getString(t, false));
  }

  @Override public void showErrorEditingAddress(Throwable t, Address address) {
    showSnackbar(errorMessageDeterminer.getString(t, false));
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    actionMode = null;
  }
}
