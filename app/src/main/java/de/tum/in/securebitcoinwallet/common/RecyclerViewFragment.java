package de.tum.in.securebitcoinwallet.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.InjectView;
import com.hannesdorfmann.mosby.dagger1.viewstate.lce.Dagger1MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingFragmentLceViewState;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import de.tum.in.securebitcoinwallet.R;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Hannes Dorfmann
 */
public abstract class RecyclerViewFragment<M extends List<?>, V extends MvpLceView<M>, P extends MvpPresenter<V>>
    extends Dagger1MvpLceViewStateFragment<SwipeRefreshLayout, M, V, P>
    implements SwipeRefreshLayout.OnRefreshListener {

  @InjectView(R.id.recyclerView) RecyclerView recyclerView;
  @InjectView(R.id.emptyView) View emptyView;
  @InjectView(R.id.fab) RapidFloatingActionButton fab;
  @Inject ErrorMessageDeterminer errorMessageDeterminer;

  protected ListAdapter<M> adapter;

  protected abstract ListAdapter<M> createAdapter();

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // RecyclerView
    adapter = createAdapter();
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setAdapter(adapter);

    // SwipeRefresh
    int[] colors = getActivity().getResources().getIntArray(R.array.loading_colors);
    contentView.setColorSchemeColors(colors);
    contentView.setOnRefreshListener(this);
  }

  @Override protected int getLayoutRes() {
    return R.layout.fragment_recyclerview;
  }

  @Override public void onRefresh() {
    loadData(true);
  }

  @Override protected String getErrorMessage(Throwable throwable, boolean b) {
    return errorMessageDeterminer.getString(throwable, b);
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

  @Override public void showContent() {

    contentView.setRefreshing(false);

    // Empty View
    if (adapter.getItemCount() == 0) {
      if (isRestoringViewState()) {
        emptyView.setVisibility(View.VISIBLE);
      } else {
        ObjectAnimator anim = ObjectAnimator.ofFloat(emptyView, "alpha", 0f, 1f).setDuration(300);
        anim.setStartDelay(250);
        anim.addListener(new AnimatorListenerAdapter() {

          @Override public void onAnimationStart(Animator animation) {
            emptyView.setVisibility(View.VISIBLE);
          }
        });
        anim.start();
      }
    } else {
      emptyView.setVisibility(View.GONE);
    }

    super.showContent();
  }

  @Override public void showLoading(boolean pullToRefresh) {
    super.showLoading(pullToRefresh);
    if (!pullToRefresh) {
      emptyView.setVisibility(View.GONE);
    }

    if (pullToRefresh && !contentView.isRefreshing()) {
      // Workaround for measure bug: https://code.google.com/p/android/issues/detail?id=77712
      contentView.post(new Runnable() {
        @Override public void run() {
          contentView.setRefreshing(true);
        }
      });
    }
  }

  @Override public void showError(Throwable e, boolean pullToRefresh) {
    super.showError(e, pullToRefresh);
    if (!pullToRefresh) {
      emptyView.setVisibility(View.GONE);
    }
    contentView.setRefreshing(false);
  }
}
