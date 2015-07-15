package de.tum.in.securebitcoinwallet.addresses;

import de.tum.in.securebitcoinwallet.common.presenter.BitcoinMvpLcePresenter;
import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.WalletManager;
import java.util.List;
import javax.inject.Inject;

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
}
