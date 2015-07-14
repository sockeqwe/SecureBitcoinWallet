package de.tum.in.securebitcoinwallet.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import butterknife.InjectView;
import com.hannesdorfmann.mosby.dagger1.viewstate.lce.Dagger1MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingFragmentLceViewState;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.view.fab.RecyclerViewScrollDetector;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Hannes Dorfmann
 */
public abstract class RecyclerViewFragment<M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
    extends Dagger1MvpLceViewStateFragment<View, M, V, P>
    implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

  @InjectView(R.id.emptyView) View emptyView;
  @InjectView(R.id.fab) RapidFloatingActionButton fab;
  @InjectView(R.id.fabMenuLayout) RapidFloatingActionLayout fabMenuLayout;

  @Inject ErrorMessageDeterminer errorMessageDeterminer;
  protected RapidFloatingActionHelper fabHelper;

  protected ListAdapter adapter;

  /**
   * Get the reference to the recyclerview
   * @return
   */
  protected RecyclerView getRecyclerView(){
    return  (RecyclerView) contentView;
  }

  /**
   * The adapter used to display the items (RecyclerView)
   *
   * @return Adapter
   */
  protected abstract ListAdapter createAdapter();

  /**
   * Get the item the Floating Action Button displays
   *
   * @return List of items
   */
  protected abstract List<RFACLabelItem> getFabMenuItems();

  /**
   * Called when a menu item has been clicked
   *
   * @param postion the index / position of the list returne in {@link #getFabMenuItems()}
   * @param item the clicked item
   */
  protected abstract void onFabMenuItemClicked(int postion, RFACLabelItem item, View view);

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // RecyclerView
    RecyclerView recyclerView = getRecyclerView();
    adapter = createAdapter();
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setAdapter(adapter);

    // SwipeRefresh

    // Fab
    RecyclerViewScrollDetector scrollDetector = new RecyclerViewScrollDetector(fab);
    recyclerView.addOnScrollListener(scrollDetector);
    RapidFloatingActionContentLabelList rfaContent =
        new RapidFloatingActionContentLabelList(getActivity());
    rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
    rfaContent.setItems(getFabMenuItems())
        .setIconShadowRadius(ABTextUtil.dip2px(getActivity(), 5))
        .setIconShadowColor(0xff888888)
        .setIconShadowDy(ABTextUtil.dip2px(getActivity(), 5));

    fabHelper =
        new RapidFloatingActionHelper(getActivity(), fabMenuLayout, fab, rfaContent).build();
  }

  @Override protected int getLayoutRes() {
    return R.layout.fragment_recyclerview;
  }

  @Override protected String getErrorMessage(Throwable throwable, boolean b) {
    return errorMessageDeterminer.getString(throwable, b);
  }

  @Override public LceViewState<M, V> createViewState() {
    return new RetainingFragmentLceViewState<>(this);
  }

  @Override public M getData() {
    return (M) adapter.getItems();
  }

  @Override public void setData(M data) {
    adapter.setItems((List) data);
    adapter.notifyDataSetChanged();
  }

  @Override public void showContent() {

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

    // Floating Action Button
    if (fab.getVisibility() != View.VISIBLE) {

      if (!isRestoringViewState()) {
        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("scaleX", 0, 1);
        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("scaleY", 0, 1);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(fab, holderX, holderY);
        animator.setInterpolator(new OvershootInterpolator());

        animator.addListener(new AnimatorListenerAdapter() {
          @Override public void onAnimationStart(Animator animation) {
            fab.setVisibility(View.VISIBLE);
          }
        });
        animator.setStartDelay(500);
        animator.start();
      } else {
        fab.setVisibility(View.VISIBLE);
      }
    }
    super.showContent();
  }

  @Override public void showLoading(boolean pullToRefresh) {
    super.showLoading(pullToRefresh);
    if (!pullToRefresh) {
      emptyView.setVisibility(View.GONE);
      fab.setVisibility(View.GONE);
    }
  }

  @Override public void showError(Throwable e, boolean pullToRefresh) {
    super.showError(e, pullToRefresh);
    if (!pullToRefresh) {
      emptyView.setVisibility(View.GONE);
      fab.setVisibility(View.GONE);
    }
  }

  @Override public void onRFACItemLabelClick(int position, RFACLabelItem item, View view) {
    onFabMenuItemClicked(position, item, view);
    //fabHelper.toggleContent();
  }

  @Override public void onRFACItemIconClick(int position, RFACLabelItem item, View view) {
    onFabMenuItemClicked(position, item, view);
    //fabHelper.toggleContent();
  }

  @Override protected void injectDependencies() {
    getObjectGraph().inject(this);
  }
}
