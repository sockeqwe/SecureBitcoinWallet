package de.tum.in.securebitcoinwallet.transactions;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
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
      views = {
          @ViewField(id = R.id.cardContainer, type = View.class, name = "card"),
          @ViewField(id = R.id.indicator, type = ImageView.class, name = "indicator"),
          @ViewField(id = R.id.name, type = TextView.class, name = "name"),
          @ViewField(id = R.id.recipient, type = TextView.class, name = "recipient"),
          @ViewField(id = R.id.bitcoins, type = TextView.class, name = "bitcoins"),
          @ViewField(id = R.id.bitcoinsCurency, type = TextView.class, name = "bitcoinsCurency")
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

    if (transaction.getSyncState() == Transaction.SYNC_NOT_SUBMITTED
        || transaction.getSyncState() == Transaction.SYNC_WAITING_CONFIRM) {
      vh.indicator.setImageResource(R.drawable.transaction_state_waiting);
    } else if (transaction.getSyncState() == Transaction.SYNC_OK) {
      vh.indicator.setImageResource(R.drawable.transaction_state_in);
    }

    if (transaction.getAmount() <= 0) {
      vh.bitcoins.setTextColor(Color.RED);
    } else {
      vh.bitcoins.setTextColor(Color.GREEN);
    }
  }
}
