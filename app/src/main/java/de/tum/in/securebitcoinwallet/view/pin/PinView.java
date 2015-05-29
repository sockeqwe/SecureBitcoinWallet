package de.tum.in.securebitcoinwallet.view.pin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import de.tum.in.securebitcoinwallet.BuildConfig;
import de.tum.in.securebitcoinwallet.R;

/**
 * A simple custom view that displays a "dot" representing a PIN
 *
 * @author Hannes Dorfmann
 */
public class PinView extends FrameLayout {

  View backgroundCircle, foregroundCircle;
  Animator animator;

  public PinView(Context context) {
    super(context);
    init();
  }

  public PinView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public PinView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @TargetApi(21)
  public PinView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  private void init() {
    backgroundCircle = new View(getContext());
    backgroundCircle.setBackgroundResource(R.drawable.pin_code_empty);
    backgroundCircle.setLayoutParams(
        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT));

    foregroundCircle = new View(getContext());
    foregroundCircle.setBackgroundResource(R.drawable.pin_code_full);
    foregroundCircle.setLayoutParams(
        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT));
    foregroundCircle.setVisibility(View.INVISIBLE);

    addView(backgroundCircle);
    addView(foregroundCircle);
  }

  /**
   * Animates the foreground view in with an animation
   *
   * @see #showFull(boolean)
   */
  public void showFull() {
    showFull(true);
  }

  /**
   * Animate the foreground view in
   *
   * @param animated true, if should run with animation, false if should be changed immediately
   */
  @TargetApi(21) public void showFull(boolean animated) {

    if (!animated) {
      foregroundCircle.setVisibility(View.VISIBLE);
      return;
    }

    AnimatorSet set = new AnimatorSet();
    set.playTogether(ObjectAnimator.ofFloat(foregroundCircle, "scaleX", 0f, 1f),
        ObjectAnimator.ofFloat(foregroundCircle, "scaleY", 0f, 1f));

    animator = set;

    animator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationStart(Animator animation) {
        foregroundCircle.setVisibility(View.VISIBLE);
      }
    });

    animator.setInterpolator(new AccelerateDecelerateInterpolator());
    animator.setDuration(200);
    animator.start();
  }

  /**
   * Animates the foreground view out with an animation.
   *
   * @see #showEmpty(boolean)
   */
  public void showEmpty() {
    showEmpty(true);
  }

  /**
   * Animates the foreground view out
   *
   * @param animated true, if should run with animation, false if should be changed immediately
   */
  @TargetApi(21) public void showEmpty(boolean animated) {

    if (!animated) {
      foregroundCircle.setVisibility(View.INVISIBLE);
      return;
    }

    if (BuildConfig.VERSION_CODE >= 21) {
      // Assumption: view is square
      animator =
          ViewAnimationUtils.createCircularReveal(foregroundCircle, backgroundCircle.getWidth() / 2,
              backgroundCircle.getHeight() / 2, backgroundCircle.getWidth(), 0);
    } else {
      AnimatorSet set = new AnimatorSet();
      set.playTogether(ObjectAnimator.ofFloat(foregroundCircle, "scaleX", 1f, 0f),
          ObjectAnimator.ofFloat(foregroundCircle, "scaleY", 1f, 0f));

      animator = set;
    }

    animator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationStart(Animator animation) {
        foregroundCircle.setVisibility(View.VISIBLE);
      }

      @Override public void onAnimationEnd(Animator animation) {
        foregroundCircle.setVisibility(View.INVISIBLE);
      }
    });

    animator.setInterpolator(new AccelerateDecelerateInterpolator());
    animator.setDuration(200);
    animator.start();
  }
}
