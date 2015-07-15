package de.tum.in.securebitcoinwallet.addresses.create.pininput;

import de.tum.in.securebitcoinwallet.common.presenter.BitcoinMvpPresenter;
import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.WalletManager;
import javax.inject.Inject;

/**
 * Presenter for controlling {@link PinInputFragment}
 *
 * @author Hannes Dorfmann
 */
public class PinInputPresenter extends BitcoinMvpPresenter<PinInputView, Address> {

  private WalletManager walletManager;

  @Inject public PinInputPresenter(WalletManager walletManager) {
    this.walletManager = walletManager;
  }

  public void createAddress(String addressName, String pin) {
    subscribe(walletManager.generateAddress(addressName, pin));
  }

  @Override protected void onError(Throwable e) {
    if (isViewAttached()){
      getView().showError(e);
    }
  }

  @Override protected void onNext(Address address) {
    if (isViewAttached()){
      getView().showCreateAddressSuccessful(address);
    }
  }
}
