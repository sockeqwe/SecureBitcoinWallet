package de.tum.in.securebitcoinwallet.transactions;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.common.ListAdapter;
import de.tum.in.securebitcoinwallet.common.RecyclerViewFragment;
import de.tum.in.securebitcoinwallet.model.presentation.TransactionList;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class TransactionsFragment
    extends RecyclerViewFragment<TransactionList, TransactionsView, TransactionsPresenter>
    implements TransactionsView {

  @Arg String address;

  private TransactionList transactionList;

  @Override protected ListAdapter createAdapter() {
    return new TransactionsAdapter(getActivity(), getObjectGraph());
  }

  @Override protected List<RFACLabelItem> getFabMenuItems() {
    Resources res = getResources();
    Drawable background = res.getDrawable(R.drawable.fab_menu_label);

    List<RFACLabelItem> items = new ArrayList<>(2);

    items.add(new RFACLabelItem<Integer>().setLabel(getString(R.string.menu_transaction_send))
        .setResId(R.drawable.ic_arrow_out)
        .setIconNormalColor(res.getColor(R.color.menu_icon2))
        .setIconPressedColor(res.getColor(R.color.menu_icon2_pressed))
        .setLabelColor(res.getColor(R.color.menu_label_text_color))
        .setLabelBackgroundDrawable(background)
        .setWrapper(3));

    items.add(new RFACLabelItem<Integer>().setLabel(getString(R.string.menu_transaction_receive))
        .setResId(R.drawable.ic_arrow_in)
        .setIconNormalColor(res.getColor(R.color.menu_icon1))
        .setIconPressedColor(res.getColor(R.color.menu_icon1_pressed))
        .setLabelColor(res.getColor(R.color.menu_label_text_color))
        .setLabelBackgroundDrawable(background)
        .setWrapper(3));

    return items;
  }

  @Override protected void onFabMeuItemClicked(int postion, RFACLabelItem item) {

  }

  @Override public TransactionsPresenter createPresenter() {
    return getObjectGraph().get(TransactionsPresenter.class);
  }

  @Override public void setData(TransactionList data) {
    this.transactionList = data;
    adapter.setItems(data.getTransactions());
    adapter.notifyDataSetChanged();
  }

  @Override public TransactionList getData() {
    return transactionList;
  }

  @Override public void loadData(boolean pullToRefresh) {
    presenter.loadTransactions(address, pullToRefresh);
  }
}
