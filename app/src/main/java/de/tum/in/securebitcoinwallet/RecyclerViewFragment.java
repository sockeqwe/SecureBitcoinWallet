package de.tum.in.securebitcoinwallet;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.InjectView;
import com.hannesdorfmann.mosby.dagger1.viewstate.lce.Dagger1MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingFragmentLceViewState;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public abstract class RecyclerViewFragment<M extends List<?>, V extends MvpLceView<M>, P extends MvpPresenter<V>>
    extends Dagger1MvpLceViewStateFragment<SwipeRefreshLayout, M, V, P>
    implements SwipeRefreshLayout.OnRefreshListener {

  @InjectView(R.id.recyclerView) RecyclerView recyclerView;

  protected ListAdapter<M> adapter;

  protected abstract ListAdapter<M> createAdapter();

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    adapter = createAdapter();
    contentView.setOnRefreshListener(this);
  }

  @Override public void onRefresh() {
    loadData(true);
  }

  @Override public LceViewState<M, V> createViewState() {
    return new RetainingFragmentLceViewState<>(this);
  }

  @Override public M getData() {
    return adapter.getItems();
  }

  @Override public void setData(M data) {
    adapter.setItems(data);
    adapter.notifyDataSetChanged();
  }
}
