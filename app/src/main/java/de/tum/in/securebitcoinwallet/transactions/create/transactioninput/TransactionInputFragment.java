package de.tum.in.securebitcoinwallet.transactions.create.transactioninput;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import butterknife.InjectView;
import butterknife.OnClick;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.mosby.dagger1.Dagger1MosbyFragment;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.addresses.create.CreateAddressActivity;
import de.tum.in.securebitcoinwallet.transactions.create.TransactionWizardData;
import de.tum.in.securebitcoinwallet.util.BitcoinUtils;
import de.tum.in.securebitcoinwallet.util.KeyboardUtils;

/**
 * Just a simple fragment to fill out a form with data to send transaction
 *
 * @author Hannes Dorfmann
 */
public class TransactionInputFragment extends Dagger1MosbyFragment {

  /**
   * Interface should be implemented by {@link CreateAddressActivity} to retrieve an update when
   * the
   * user has inserted a valid address receiverEditText
   */
  public interface TransactionInputListner {

    /**
     * Valid address receiverEditText chosen, go ahead with entering the pin
     *
     * @param wizardData The desired receiverEditText for the address
     */
    public void onTransactionWizardFilled(TransactionWizardData wizardData);
  }

  @Arg(required = false) TransactionWizardData wizardData;

  private TransactionInputListner listener;

  @InjectView(R.id.receiver) EditText receiverEditText;
  @InjectView(R.id.amount) EditText amountBitcoins;
  @InjectView(R.id.reference) EditText referenceEditText;
  @InjectView(R.id.next) View nextButton;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  @Override protected int getLayoutRes() {
    return R.layout.fragment_create_tranasction_input;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (wizardData != null && savedInstanceState == null) {
      if (wizardData.getReceiverAddress() != null) {
        receiverEditText.setText(wizardData.getReceiverAddress());
      }

      long satoshi = wizardData.getSatoshi();
      if (satoshi > 0) {
        amountBitcoins.setText(Long.toString(satoshi));
      }

      if (wizardData.getReference() != null) {
        referenceEditText.setText(wizardData.getReference());
      }
    }
  }

  @OnClick(R.id.next) public void onNextClicked() {

    receiverEditText.clearAnimation();
    amountBitcoins.clearAnimation();
    referenceEditText.clearAnimation();

    // receiver check
    String address = receiverEditText.getText().toString();
    if (TextUtils.isEmpty(address) || !BitcoinUtils.validateBitcoinAddress(
        receiverEditText.getText().toString())) {
      Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
      receiverEditText.startAnimation(shake);
      return;
    }

      // amount satoshi check
      long satoshi = 0;
      try {
        satoshi = Long.parseLong(amountBitcoins.getText().toString());
      } catch (Throwable e) {
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        amountBitcoins.startAnimation(shake);
        return;
      }

      // Reference check
      if (TextUtils.isEmpty(referenceEditText.getText().toString())) {
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        referenceEditText.startAnimation(shake);
        return;
      }

      final TransactionWizardData wizardData = new TransactionWizardData();
      wizardData.setReceiverAddress(receiverEditText.getEditableText().toString());
      wizardData.setSatoshi(satoshi);
      wizardData.setReference(referenceEditText.getText().toString());

      nextButton.setClickable(false);
      KeyboardUtils.hideKeyboard(receiverEditText);
      receiverEditText.postDelayed(new Runnable() {
        @Override public void run() {
          nextButton.setClickable(true);
          listener.onTransactionWizardFilled(wizardData);
        }
      }, 100);
    }

    @Override public void onAttach (Activity activity){
      super.onAttach(activity);
      if (!(activity instanceof TransactionInputListner)) {
        throw new IllegalArgumentException(
            "Activity must implement " + TransactionInputListner.class.getSimpleName());
      }

      listener = (TransactionInputListner) activity;
    }
  }
