package de.tum.in.securebitcoinwallet.addresses.create.pininput;

import android.widget.Toast;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.lock.LockFragment;
import de.tum.in.securebitcoinwallet.model.Address;

/**
 * A Fragment for inserting a PIN . After entering the pin correctly a new Address will be created.
 *
 * @author Hannes Dorfmann
 */
public class PinInputFragment extends LockFragment<PinInputView, PinInputPresenter>
    implements PinInputView {

  @Arg String addressName;

  @Override protected void onPinInserted(String pin) {
    presenter.createAddress(addressName, pin);
  }

  @Override public PinInputPresenter createPresenter() {
    return getObjectGraph().get(PinInputPresenter.class);
  }

  @Override public void showCreateAddressSuccessful(Address address) {
    Toast.makeText(getActivity(), R.string.create_address_successful, Toast.LENGTH_LONG).show();
    getActivity().finish();
  }
}
