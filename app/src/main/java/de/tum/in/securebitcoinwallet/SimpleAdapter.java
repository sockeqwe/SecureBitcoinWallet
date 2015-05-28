package de.tum.in.securebitcoinwallet;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class SimpleAdapter<I extends List<?>> extends RecyclerView.Adapter {

  private List<I> items;

  public List<I> getItems() {
    return items;
  }

  public void setItems(List<I> items) {
    this.items = items;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    return null;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

  }

  @Override public int getItemCount() {
    return 0;
  }
}
