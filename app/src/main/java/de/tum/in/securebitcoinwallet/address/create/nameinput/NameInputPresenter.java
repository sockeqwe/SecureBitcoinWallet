package de.tum.in.securebitcoinwallet.address.create.nameinput;

import de.tum.in.securebitcoinwallet.common.presenter.BitcoinMvpPresenter;
import de.tum.in.securebitcoinwallet.model.database.AddressDao;
import javax.inject.Inject;

/**
 * Presenter that controlls {@link NameInputView} and checks if the address view is available
 *
 * @author Hannes Dorfmann
 */
public class NameInputPresenter extends BitcoinMvpPresenter<NameInputView, Boolean> {

  private AddressDao addressDao;

  @Inject public NameInputPresenter(AddressDao addressDao) {
    this.addressDao = addressDao;
  }

  public void checkAddressName(String addressName) {
    subscribe(addressDao.isAddressNameAvailable(addressName));
  }

  @Override protected void onError(Throwable e) {
    if (isViewAttached()) {
      getView().showAddressNameAlreadyInUse();
    }
  }

  @Override protected void onNext(Boolean aBoolean) {
    if (isViewAttached()) {
      getView().showAddressNameValid();
    }
  }
}
