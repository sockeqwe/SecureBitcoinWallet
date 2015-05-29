package de.tum.in.securebitcoinwallet.view.fab;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import de.tum.in.securebitcoinwallet.util.DimensUtils;

/**
 * This class is responsible to listen for scroll events and to animate the FloatingActionButton in
 * and out
 */
public class RecyclerViewScrollDetector extends RecyclerView.OnScrollListener {

  private ScrollDirectionListener mScrollDirectionListener;
  private RecyclerView.OnScrollListener mOnScrollListener;
  private View fab;
  private boolean mVisible = true;
  private Animator animator;

  protected int mScrollThreshold;

  private Interpolator interpolator = new AccelerateDecelerateInterpolator();

  public RecyclerViewScrollDetector(View fab) {
    this.mOnScrollListener = mOnScrollListener;
    this.fab = fab;
    mScrollThreshold = DimensUtils.dpToPx(fab.getContext(), 4);
  }

  private void onScrollDown() {
    show();
    if (mScrollDirectionListener != null) {
      mScrollDirectionListener.onScrollDown();
    }
  }

  private void onScrollUp() {
    hide();
    if (mScrollDirectionListener != null) {
      mScrollDirectionListener.onScrollUp();
    }
  }

  public void setScrollDirectionListener(ScrollDirectionListener mScrollDirectionListener) {
    this.mScrollDirectionListener = mScrollDirectionListener;
  }

  @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
    if (isSignificantDelta) {
      if (dy > 0) {
        onScrollUp();
      } else {
        onScrollDown();
      }
    }

    if (mOnScrollListener != null) {
      mOnScrollListener.onScrolled(recyclerView, dx, dy);
    }

    super.onScrolled(recyclerView, dx, dy);
  }

  @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    if (mOnScrollListener != null) {
      mOnScrollListener.onScrollStateChanged(recyclerView, newState);
    }

    super.onScrollStateChanged(recyclerView, newState);
  }

  private void show() {
    toggle(true, true, false);
  }

  private void hide() {
    toggle(false, true, false);
  }

  private void toggle(final boolean visible, final boolean animate, boolean force) {
    if (mVisible != visible || force) {
      mVisible = visible;
      int height = fab.getHeight();

      if (animator != null) {
        animator.cancel();
      }

      int translationY = visible ? 0 : height + getMarginBottom();
      if (animate) {
        animator = ObjectAnimator.ofFloat(fab, "translationY", translationY);
        animator.setInterpolator(interpolator);
        animator.setDuration(200);
        animator.start();
      } else {
        fab.setTranslationY(translationY);
      }
    }
  }

  /**
   * Get the margint bottom of the fab
   *
   * @return bottom margin in px
   */
  private int getMarginBottom() {
    int marginBottom = 0;
    final ViewGroup.LayoutParams layoutParams = fab.getLayoutParams();
    if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
      marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
    }
    return marginBottom;
  }
}