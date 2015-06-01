package de.tum.in.securebitcoinwallet.transactions;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.hannesdorfmann.annotatedadapter.annotation.Field;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import dagger.ObjectGraph;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.common.ListAdapter;
import de.tum.in.securebitcoinwallet.model.CurrencyManager;
import de.tum.in.securebitcoinwallet.model.Transaction;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Hannes Dorfmann
 */
public class TransactionsAdapter extends ListAdapter<List<Transaction>>
    implements TransactionsAdapterBinder {

  @ViewType(layout = R.layout.listelement_transaction,
      fields = {
          @Field(id = R.id.cardContainer, type= View.class, name="card"),
          @Field(id = R.id.indicator, type = ImageView.class, name = "indicator"),
          @Field(id = R.id.name, type = TextView.class, name = "name"),
          @Field(id = R.id.recipient, type = TextView.class, name = "recipient"),
          @Field(id = R.id.bitcoins, type = TextView.class, name = "bitcoins"),
          @Field(id = R.id.bitcoinsCurency, type = TextView.class, name = "bitcoinsCurency")
      }) public final int transaction = 0;

  @Inject CurrencyManager currencyManager;

  public TransactionsAdapter(Context context, ObjectGraph objectGraph) {
    super(context);
    objectGraph.inject(this);
  }

  @Override
  public void bindViewHolder(TransactionsAdapterHolders.TransactionViewHolder vh, int position) {
    Transaction transaction = items.get(position);
    vh.name.setText(transaction.getName());
    vh.bitcoins.setText(currencyManager.satoshiToBitcoin(transaction.getAmount()));
    vh.bitcoinsCurency.setText(currencyManager.toCustomCurrency(transaction.getAmount()));
    vh.recipient.setText(transaction.getAddress());
    vh.card.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        // TODO implement? Open details?
      }
    });
  }
}
