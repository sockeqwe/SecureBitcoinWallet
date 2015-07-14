package de.tum.in.securebitcoinwallet.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import at.markushi.ui.RevealColorView;
import butterknife.InjectView;
import com.hannesdorfmann.mosby.dagger1.Dagger1MosbyActivity;
import de.tum.in.securebitcoinwallet.R;

/**
 * Activity that can be started with an reveal animation
 *
 * @author Hannes Dorfmann
 */
public class CircleRevealActivity extends Dagger1MosbyActivity {

  public static final String KEY_REVEAL_X = "de.tum.in.securebitcoinwallet.common.X";
  public static final String KEY_REVEAL_Y = "de.tum.in.securebitcoinwallet.common.Y";
  private static final String KEY_RUN_ANIMATION = "de.tum.in.securebitcoinwallet.common.animation";

  @InjectView(R.id.contentContainer) public ViewGroup contentContainer;
  @InjectView(R.id.background) public RevealColorView background;
  @InjectView(R.id.toolbar) public Toolbar toolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutRes());

    toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    DrawableCompat.setTint(toolbar.getNavigationIcon(), Color.WHITE);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    setSupportActionBar(toolbar);

    overridePendingTransition(0, 0);
    setupEnterAnimation();
  }

  /**
   * Get the layout resource that should be used fot this activity
   *
   * @return The laout resource id like R.layout.my_layout
   */
  protected int getLayoutRes() {
    return R.layout.activity_reveal;
  }

  /**
   * Sets up the enter reveal animation and start the animation
   */
  private void setupEnterAnimation() {

    boolean runAnimation = getIntent().getBooleanExtra(KEY_RUN_ANIMATION, true);

    if (runAnimation) {
      getIntent().putExtra(KEY_RUN_ANIMATION, false);
      background.getViewTreeObserver()
          .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override public boolean onPreDraw() {

              background.getViewTreeObserver().removeOnPreDrawListener(this);

              int x = getIntent().getIntExtra(KEY_REVEAL_X, -1);
              int y = getIntent().getIntExtra(KEY_REVEAL_Y, -1);

              if (x == -1 || y == -1) {
                x = background.getWidth() / 2;
                y = background.getHeight() / 2;
              }
              // Setup animation
              background.reveal(x, y, getBackgroundColor(), 0, 800, new AnimatorListenerAdapter() {

                @Override public void onAnimationEnd(Animator animation) {

                  // final int translY = -DimensUtils.dpToPx(CircleRevealActivity.this, 20);

                  AnimatorSet set = new AnimatorSet();
                  set.playTogether(
                      //ObjectAnimator.ofFloat(contentContainer, "translationY", translY, 0),
                      ObjectAnimator.ofFloat(contentContainer, "alpha", 0f, 1f));

                  set.setInterpolator(new OvershootInterpolator());
                  set.setDuration(1000);
                  set.setStartDelay(300);
                  set.addListener(new AnimatorListenerAdapter() {
                    @Override public void onAnimationStart(Animator animation) {
                      contentContainer.setAlpha(0);
                      // contentContainer.setTranslationY(translY);
                      contentContainer.setVisibility(View.VISIBLE);
                    }
                  });

                  set.start();
                }
              });

              return true;
            }
          });
    } else {
      background.setBackgroundColor(getBackgroundColor());
      contentContainer.setVisibility(View.VISIBLE);
    }
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(0, R.anim.zoom_out);
  }

  /**
   * Get the color that should be used as background resource for this activity
   *
   * @return the color (not color resource id)s
   */
  protected int getBackgroundColor() {
    return getResources().getColor(R.color.primary);
  }
}
