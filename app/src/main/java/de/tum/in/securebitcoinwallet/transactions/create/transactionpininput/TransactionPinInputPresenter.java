package de.tum.in.securebitcoinwallet.transactions.create.transactionpininput;

import de.tum.in.securebitcoinwallet.common.presenter.BitcoinMvpPresenter;
import de.tum.in.securebitcoinwallet.model.Transaction;
import de.tum.in.securebitcoinwallet.model.WalletManager;
import de.tum.in.securebitcoinwallet.transactions.create.TransactionWizardData;
import javax.inject.Inject;

/**
 * Presenter for controlling {@link TransactionPinInputFragment}
 *
 * @author Hannes Dorfmann
 */
public class TransactionPinInputPresenter
    extends BitcoinMvpPresenter<TransactionPinInputView, Transaction> {

  private WalletManager walletManager;

  @Inject public TransactionPinInputPresenter(WalletManager walletManager) {
    this.walletManager = walletManager;
  }

  public void createTransaction(String pin, String address, TransactionWizardData data) {
    if (isViewAttached()) {
      getView().showLoading();
      subscribe(walletManager.sendTransaction(pin, address, data));
    }
  }

  @Override protected void onError(Throwable e) {
    if (isViewAttached()) {
      getView().showError(e);
    }
  }

  @Override protected void onNext(Transaction address) {
    if (isViewAttached()) {
      getView().showCreatingTransactionSuccessful(address);
    }
  }
}
