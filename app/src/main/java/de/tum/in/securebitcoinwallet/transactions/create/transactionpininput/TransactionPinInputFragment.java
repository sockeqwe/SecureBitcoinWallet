package de.tum.in.securebitcoinwallet.transactions.create.transactionpininput;

import android.widget.Toast;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.lock.LockFragment;
import de.tum.in.securebitcoinwallet.model.Transaction;
import de.tum.in.securebitcoinwallet.transactions.create.TransactionWizardData;

/**
 * A Fragment for inserting a PIN . After entering the pin correctly a new Address will be created.
 *
 * @author Hannes Dorfmann
 */
public class TransactionPinInputFragment
    extends LockFragment<TransactionPinInputView, TransactionPinInputPresenter>
    implements TransactionPinInputView {

  @Arg TransactionWizardData wizardData;

  @Override protected void onPinInserted(String pin) {
    presenter.createAddress(wizardData);
  }

  @Override public TransactionPinInputPresenter createPresenter() {
    return getObjectGraph().get(TransactionPinInputPresenter.class);
  }

  @Override public void showCreatingAddressSuccessful(Transaction transaction) {
    Toast.makeText(getActivity(), R.string.create_address_successful, Toast.LENGTH_LONG).show();
    getActivity().finish();
  }
}
