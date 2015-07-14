package de.tum.in.securebitcoinwallet.accounts;

import de.tum.in.securebitcoinwallet.common.presenter.BitcoinMvpLcePresenter;
import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.WalletManager;
import java.util.List;
import javax.inject.Inject;

/**
 * The presenter responsible to coordinate the {@link AccountListView}
 *
 * @author Hannes Dorfmann
 */
public class AccountListPresenter extends BitcoinMvpLcePresenter<AccountListView, List<Address>> {

  private WalletManager walletManager;

  @Inject public AccountListPresenter(WalletManager walletManager) {
    this.walletManager = walletManager;
  }

  public void loadList() {
    subscribe(walletManager.getMyAddresses());
  }
}
