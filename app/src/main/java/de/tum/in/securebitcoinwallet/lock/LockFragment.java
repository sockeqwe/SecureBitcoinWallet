package de.tum.in.securebitcoinwallet.lock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import com.hannesdorfmann.mosby.dagger1.viewstate.Dagger1MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import de.tum.in.securebitcoinwallet.common.ErrorMessageDeterminer;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.util.DimensUtils;
import de.tum.in.securebitcoinwallet.view.pin.PinView;
import javax.inject.Inject;

/**
 * Abstract implementation of a fragment that dispalys a view to insert a PIN
 *
 * @author Hannes Dorfmann
 */
public abstract class LockFragment<V extends LockView, P extends MvpPresenter<V>>
    extends Dagger1MvpViewStateFragment<V, P> implements LockView {

  @Inject ErrorMessageDeterminer messageDeterminer;

  @InjectView(R.id.logo) @Optional View logo;
  @InjectView(R.id.rootLayout) ViewGroup rootLayout;
  @InjectView(R.id.errorView) TextView errorView;
  @InjectView(R.id.pinContainer) View pinContainer;
  @InjectView(R.id.pin1) PinView pin1;
  @InjectView(R.id.pin2) PinView pin2;
  @InjectView(R.id.pin3) PinView pin3;
  @InjectView(R.id.pin4) PinView pin4;
  @InjectView(R.id.pinLine) View pinLine;
  @InjectView(R.id.button0) View button0;
  @InjectView(R.id.button1) View button1;
  @InjectView(R.id.button2) View button2;
  @InjectView(R.id.button3) View button3;
  @InjectView(R.id.button4) View button4;
  @InjectView(R.id.button5) View button5;
  @InjectView(R.id.button6) View button6;
  @InjectView(R.id.button7) View button7;
  @InjectView(R.id.button8) View button8;
  @InjectView(R.id.button9) View button9;
  @InjectView(R.id.buttonDelete) View deleteButton;

  private Animator loadingAnimator;
  private StringBuffer pin;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  @Override protected int getLayoutRes() {
    return R.layout.fragment_lock;
  }

  @SuppressLint("NewApi") @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    pin = new StringBuffer();
    ;

    if (Build.VERSION.SDK_INT >= 16) {
      int startDelay = 0;
      LayoutTransition transition = new LayoutTransition();
      transition.enableTransitionType(LayoutTransition.CHANGING);
      transition.setStartDelay(LayoutTransition.APPEARING, startDelay);
      transition.setStartDelay(LayoutTransition.CHANGE_APPEARING, startDelay);
      rootLayout.setLayoutTransition(transition);
    }

    if (loadingAnimator != null) {
      loadingAnimator.cancel();
    }
  }

  @Override public ViewState createViewState() {
    return new LockViewState();
  }

  @Override public void onNewViewStateInstance() {
    showInput();
  }

  @Override public void showError(Throwable t) {
    ((LockViewState) viewState).setShowingError(t);
    errorView.setText(messageDeterminer.getString(t, false));
    errorView.setVisibility(View.VISIBLE);
    if (loadingAnimator != null) {
      loadingAnimator.cancel();
    }
    setNumbersEnabled(true);

    if (!isRestoringViewState()) {
      Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
      pinContainer.clearAnimation();
      pinLine.clearAnimation();
      errorView.clearAnimation();
      pinContainer.startAnimation(shake);
      pinLine.startAnimation(shake);
      errorView.startAnimation(shake);
      if (logo != null) {
        logo.clearAnimation();
        logo.startAnimation(shake);
      }
      pin1.showEmpty();
      pin2.showEmpty();
      pin3.showEmpty();
      pin4.showEmpty();
    }
  }

  @Override public void showInput() {
    ((LockViewState) viewState).setShowingInput();
    setNumbersEnabled(true);
  }

  @Override public void showLoading() {
    ((LockViewState) viewState).setShowingLoading();
    errorView.setVisibility(View.GONE);
    pinContainer.clearAnimation();
    pinLine.clearAnimation();
    setNumbersEnabled(false);

    if (isRestoringViewState()) {
      pin1.showFull(false);
      pin2.showFull(false);
      pin3.showFull(false);
      pin4.showFull(false);
    }

    AnimatorSet set = new AnimatorSet();
    set.playTogether(buildPinAnimator(pin1, 400, 0), buildPinAnimator(pin2, 400, 100),
        buildPinAnimator(pin3, 400, 200), buildPinAnimator(pin4, 300, 400));

    loadingAnimator = set;
    loadingAnimator.addListener(new AnimatorListenerAdapter() {
      boolean canceled = false;

      @Override public void onAnimationCancel(Animator animation) {
        canceled = true;

        if (pin1 != null) {
          pin1.setTranslationY(0);
          pin2.setTranslationY(0);
          pin3.setTranslationY(0);
          pin4.setTranslationY(0);
        }
      }

      @Override public void onAnimationEnd(Animator animation) {
        if (!canceled && loadingAnimator != null) {
          loadingAnimator.start();
        }
      }
    });

    loadingAnimator.start();
  }

  private Animator buildPinAnimator(View pin, int duration, int startDelay) {

    int yDiff = DimensUtils.dpToPx(getActivity(), 20);

    AnimatorSet set = new AnimatorSet();
    Animator up = ObjectAnimator.ofFloat(pin, "translationY", 0, -yDiff);
    up.setInterpolator(new OvershootInterpolator());
    Animator down = ObjectAnimator.ofFloat(pin, "translationY", -yDiff, 0);
    up.setInterpolator(new DecelerateInterpolator());

    set.playSequentially(up, down);
    set.setDuration(duration);
    set.setStartDelay(startDelay);

    return set;
  }

  private void checkPin() {
    int length = pin.length();

    if (length == 1) {
      pin1.showFull();
    } else if (length == 2) {
      pin2.showFull();
    } else if (length == 3) {
      pin3.showFull();
    } else if (length == 4) {
      pin4.showFull();
      onPinInserted(pin.toString());
      pin = new StringBuffer();
    }
  }

  /**
   * Called once the pin has been entered
   *
   * @param pin The pin
   */
  protected abstract void onPinInserted(String pin);

  @OnClick(R.id.button0) public void onButton0() {
    pin.append('0');
    checkPin();
  }

  @OnClick(R.id.button1) public void onButton1() {
    pin.append('1');
    checkPin();
  }

  @OnClick(R.id.button2) public void onButton2() {
    pin.append('2');
    checkPin();
  }

  @OnClick(R.id.button3) public void onButton3() {
    pin.append('3');
    checkPin();
  }

  @OnClick(R.id.button4) public void onButton4() {
    pin.append('4');
    checkPin();
  }

  @OnClick(R.id.button5) public void onButton5() {
    pin.append('5');
    checkPin();
  }

  @OnClick(R.id.button6) public void onButton6() {
    pin.append('6');
    checkPin();
  }

  @OnClick(R.id.button7) public void onButton7() {
    pin.append('7');
    checkPin();
  }

  @OnClick(R.id.button8) public void onButton8() {
    pin.append('8');
    checkPin();
  }

  @OnClick(R.id.button9) public void onButton9() {
    pin.append('9');
    checkPin();
  }

  @OnClick(R.id.buttonDelete) public void onDeletaButton() {
    int length = pin.length();
    if (length > 0) {
      pin.deleteCharAt(pin.length() - 1);
      length = pin.length();
      if (length == 0) {
        pin1.showEmpty();
      } else if (length == 1) {
        pin2.showEmpty();
      } else if (length == 2) {
        pin3.showEmpty();
      } else if (length == 3) {
        pin4.showEmpty();
      }
    }
  }

  /**
   * Enables or disables the number buttons
   *
   * @param enabled true to enable
   */
  private void setNumbersEnabled(boolean enabled) {
    button0.setEnabled(enabled);
    button1.setEnabled(enabled);
    button2.setEnabled(enabled);
    button3.setEnabled(enabled);
    button4.setEnabled(enabled);
    button5.setEnabled(enabled);
    button6.setEnabled(enabled);
    button7.setEnabled(enabled);
    button8.setEnabled(enabled);
    button9.setEnabled(enabled);
    deleteButton.setEnabled(enabled);
  }

  @Override public boolean isChangingConfigurations() {
    return getActivity() != null && getActivity().isChangingConfigurations();
  }
}
