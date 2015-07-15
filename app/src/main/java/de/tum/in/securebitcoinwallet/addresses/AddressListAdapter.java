package de.tum.in.securebitcoinwallet.addresses;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hannesdorfmann.annotatedadapter.annotation.Field;
import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import dagger.ObjectGraph;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.common.ListAdapter;
import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.CurrencyManager;
import java.util.List;
import javax.inject.Inject;

/**
 * Adapter displaying a list of Accounts
 *
 * @author Hannes Dorfmann
 */
public class AddressListAdapter extends ListAdapter<List<Address>>
    implements AddressListAdapterBinder {

  /**
   * Click listener for account items
   */
  static class InternalAddressClickListener implements View.OnClickListener {

    AddressClickListener clickListener;
    public Address address;

    public InternalAddressClickListener(AddressClickListener clickListener) {
      this.clickListener = clickListener;
    }

    @Override public void onClick(View v) {
      clickListener.onAddressClicked(address);
    }
  }

  static class InternalAccountLongClickListener implements View.OnLongClickListener {

    public Address address;
    private AddressLongClickListener listener;

    public InternalAccountLongClickListener(AddressLongClickListener listener) {
      this.listener = listener;
    }

    @Override public boolean onLongClick(View v) {
      listener.onAddressLongClicked(address);
      return true;
    }
  }

  @ViewType(layout = R.layout.listelement_address,
      initMethod = true,
      views = {
          @ViewField(type = TextView.class, name = "name", id = R.id.textViewAccountName),
          @ViewField(type = TextView.class, name = "address", id = R.id.textViewAddress),
          @ViewField(type = TextView.class, name = "amount", id = R.id.amountBitcoin),
          @ViewField(type = TextView.class, name = "amountCustomCurrency", id = R.id.amountCustomCurrency)
      },
      fields = {
          @Field(type = InternalAddressClickListener.class, name = "clickListener"),
          @Field(type = InternalAccountLongClickListener.class, name = "longClickListener")
      }) public final int address = 0;

  private Context context;
  private AddressLongClickListener longClickListener;
  private AddressClickListener clickListener;

  private Address selectedItem;
  private int colorSelectedTitle;
  private int colorSelectedSubtitle;
  private int colorNormalTitle;
  private int colorNormalSubtitle;

  @Inject CurrencyManager currencyManager;

  public AddressListAdapter(Context context, ObjectGraph objectGraph,
      AddressLongClickListener longClickListener, AddressClickListener clickListener) {
    super(context);
    this.context = context;
    this.longClickListener = longClickListener;
    this.clickListener = clickListener;

    colorSelectedSubtitle = context.getResources().getColor(R.color.accent);
    colorSelectedTitle = context.getResources().getColor(R.color.accent_dark);
    colorNormalTitle = context.getResources().getColor(R.color.primary_text);
    colorNormalSubtitle = context.getResources().getColor(R.color.secondary_text);
    objectGraph.inject(this);
  }

  @Override public void initViewHolder(AddressListAdapterHolders.AddressViewHolder vh, View view,
      ViewGroup parent) {
    vh.clickListener = new InternalAddressClickListener(clickListener);
    vh.longClickListener = new InternalAccountLongClickListener(longClickListener);

    vh.itemView.setOnClickListener(vh.clickListener);
    vh.itemView.setOnLongClickListener(vh.longClickListener);
  }

  @Override
  public void bindViewHolder(AddressListAdapterHolders.AddressViewHolder vh, int position) {
    final Address address = items.get(position);
    vh.name.setText(address.getName());
    vh.address.setText(address.getAddress());
    vh.amount.setText(currencyManager.satoshiToBitcoin(address.getAmount()));
    vh.amountCustomCurrency.setText(currencyManager.toCustomCurrency(address.getAmount()));

    vh.clickListener.address = address;
    vh.longClickListener.address = address;

    if (address == selectedItem) {
      vh.address.setTextColor(colorSelectedTitle);
      vh.name.setTextColor(colorSelectedSubtitle);
    } else {
      vh.address.setTextColor(colorNormalSubtitle);
      vh.name.setTextColor(colorNormalTitle);
    }
  }

  public void setSelectedItem(Address selectedItem) {
    this.selectedItem = selectedItem;
  }
}
