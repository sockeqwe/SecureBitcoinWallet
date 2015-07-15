package de.tum.in.securebitcoinwallet.addresses;

import com.hannesdorfmann.mosby.mvp.rx.scheduler.AndroidSchedulerTransformer;
import de.tum.in.securebitcoinwallet.common.presenter.BitcoinMvpLcePresenter;
import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.WalletManager;
import java.util.List;
import javax.inject.Inject;
import rx.Subscriber;

/**
 * The presenter responsible to coordinate the {@link AddressListView}
 *
 * @author Hannes Dorfmann
 */
public class AddressListPresenter extends BitcoinMvpLcePresenter<AddressListView, List<Address>> {

  private WalletManager walletManager;

  @Inject public AddressListPresenter(WalletManager walletManager) {
    this.walletManager = walletManager;
  }

  public void loadList() {
    subscribe(walletManager.getMyAddresses());
  }

  public void deleteAddress(final Address address) {

    walletManager.deleteAddress(address)
        .compose(new AndroidSchedulerTransformer<Boolean>())
        .subscribe(new Subscriber<Boolean>() {
          @Override public void onCompleted() {
            // nothing to do

          }

          @Override public void onError(Throwable e) {
            if (isViewAttached()) {
              getView().showErrorDeleteingAddress(e, address);
            }
          }

          @Override public void onNext(Boolean aBoolean) {
            // Nothing to do
          }
        });
  }

  public void renameAddress(Address address, String newName) {
    // TODO implement
  }
}
