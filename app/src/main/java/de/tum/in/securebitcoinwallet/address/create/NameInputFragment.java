package de.tum.in.securebitcoinwallet.address.create;

import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import butterknife.InjectView;
import butterknife.OnClick;
import com.hannesdorfmann.mosby.MosbyFragment;
import de.tum.in.securebitcoinwallet.R;

/**
 * Just a simple fragment that
 *
 * @author Hannes Dorfmann
 */
public class NameInputFragment extends MosbyFragment {

  @InjectView(R.id.name) EditText name;

  @Override protected int getLayoutRes() {
    return R.layout.fragment_create_name_input;
  }

  @OnClick(R.id.next) public void onNextClicked() {

    name.clearAnimation();
    if (TextUtils.isEmpty(name.getText())) {
      Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
      name.startAnimation(shake);
    } else {
      // everything is ok, insert
      // TODO implement
    }
  }
}
