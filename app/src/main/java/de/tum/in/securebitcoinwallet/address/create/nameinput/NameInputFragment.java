package de.tum.in.securebitcoinwallet.address.create.nameinput;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import butterknife.InjectView;
import butterknife.OnClick;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.mosby.dagger1.mvp.Dagger1MvpFragment;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.address.create.CreateAddressActivity;
import de.tum.in.securebitcoinwallet.util.KeyboardUtils;

/**
 * Just a simple fragment that
 *
 * @author Hannes Dorfmann
 */
public class NameInputFragment extends Dagger1MvpFragment<NameInputView, NameInputPresenter>
    implements NameInputView {

  /**
   * Interface should be implemented by {@link CreateAddressActivity} to retrieve an update when
   * the
   * user has inserted a valid address nameEditText
   */
  public interface NameInputListner {

    /**
     * Valid address nameEditText chosen, go ahead with entering the pin
     *
     * @param name The desired nameEditText for the address
     */
    public void validAddressNameInserted(String name);
  }

  @Arg(required = false) String name;

  private NameInputListner listener;
  private Snackbar snackbar;
  @InjectView(R.id.name) EditText nameEditText;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  @Override protected int getLayoutRes() {
    return R.layout.fragment_create_name_input;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (name != null && savedInstanceState == null) {
      nameEditText.setText(name);
    }
  }

  @OnClick(R.id.next) public void onNextClicked() {

    nameEditText.clearAnimation();
    if (TextUtils.isEmpty(nameEditText.getText().toString())) {
      Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
      nameEditText.startAnimation(shake);
    } else {
      KeyboardUtils.hideKeyboard(nameEditText);
      presenter.checkAddressName(nameEditText.getText().toString());
    }
  }

  @Override public void showAddressNameValid() {
    listener.validAddressNameInserted(nameEditText.getText().toString());
  }

  @Override public void showAddressNameAlreadyInUse() {

    if (snackbar != null) {
      snackbar.dismiss();
    }

    snackbar = Snackbar.make(getView(), R.string.error_create_address_name_already_in_use,
        Snackbar.LENGTH_SHORT);
    snackbar.show();
  }

  @Override public boolean isChangingConfigurations() {
    return getActivity() != null && getActivity().isChangingConfigurations();
  }

  @Override public NameInputPresenter createPresenter() {
    return getObjectGraph().get(NameInputPresenter.class);
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (!(activity instanceof NameInputListner)) {
      throw new IllegalArgumentException(
          "Activity must implement " + NameInputListner.class.getSimpleName());
    }

    listener = (NameInputListner) activity;
  }
}
