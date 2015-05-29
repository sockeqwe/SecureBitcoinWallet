package de.tum.in.securebitcoinwallet.accountlist;

import com.hannesdorfmann.mosby.mvp.rx.lce.MvpLceRxPresenter;
import de.greenrobot.event.EventBus;
import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.WalletManager;
import java.util.List;
import javax.inject.Inject;

/**
 * The presenter responsible to coordinate the {@link AccountListView}
 * @author Hannes Dorfmann
 */
public class AccountListPresenter extends MvpLceRxPresenter<AccountListView, List<Address>> {

  private WalletManager walletManager;
  private EventBus eventBus;

  @Inject public AccountListPresenter(WalletManager walletManager, EventBus eventBus) {
    this.walletManager = walletManager;
    this.eventBus = eventBus;
  }

  public void loadList(boolean pullToRefresh) {
    subscribe(walletManager.getMyAddresses(), pullToRefresh);
  }
}
