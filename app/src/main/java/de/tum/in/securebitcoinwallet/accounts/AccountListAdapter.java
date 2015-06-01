package de.tum.in.securebitcoinwallet.accounts;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.hannesdorfmann.annotatedadapter.annotation.Field;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import dagger.ObjectGraph;
import de.tum.in.securebitcoinwallet.IntentStarter;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.common.ListAdapter;
import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.CurrencyManager;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Hannes Dorfmann
 */
public class AccountListAdapter extends ListAdapter<List<Address>>
    implements AccountListAdapterBinder {

  @ViewType(layout = R.layout.listelement_address,
      fields = {
          @Field(type = TextView.class, name = "name", id = R.id.textViewAccountName),
          @Field(type = TextView.class, name = "address", id = R.id.textViewAddress),
          @Field(type = TextView.class, name = "amount", id = R.id.amountBitcoin),
          @Field(type = TextView.class, name = "amountCustomCurrency", id = R.id.amountCustomCurrency)
      }) public final int address = 0;

  private Context context;
  @Inject CurrencyManager currencyManager;

  @Inject IntentStarter intentStarter;

  public AccountListAdapter(Context context, ObjectGraph objectGraph) {
    super(context);
    this.context = context;
    objectGraph.inject(this);
  }

  @Override
  public void bindViewHolder(AccountListAdapterHolders.AddressViewHolder vh, int position) {
    final Address address = items.get(position);
    vh.name.setText(address.getName());
    vh.address.setText(address.getAddress());
    vh.amount.setText(currencyManager.satoshiToBitcoin(address.getAmount()));
    vh.amountCustomCurrency.setText(currencyManager.toCustomCurrency(address.getAmount()));
    vh.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        intentStarter.showTransactions(context, address.getAddress());
      }
    });
  }
}
