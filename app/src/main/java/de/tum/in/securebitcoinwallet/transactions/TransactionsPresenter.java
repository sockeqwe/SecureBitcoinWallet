package de.tum.in.securebitcoinwallet.transactions;

import de.tum.in.securebitcoinwallet.common.presenter.BitcoinMvpLcePresenter;
import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.Transaction;
import de.tum.in.securebitcoinwallet.model.WalletManager;
import de.tum.in.securebitcoinwallet.model.presentation.TransactionList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Func2;

/**
 * @author Hannes Dorfmann
 */
public class TransactionsPresenter extends
    BitcoinMvpLcePresenter<TransactionsView, TransactionList> {

  private WalletManager walletManager;

  @Inject public TransactionsPresenter(WalletManager walletManager) {
    this.walletManager = walletManager;
  }

  public void loadTransactions(String address) {

    // Get address details and list of transactions and combine them to one TransactionList
    Observable<TransactionList> observable =
        Observable.combineLatest(walletManager.getAddress(address),
            walletManager.getTransactions(address),
            new Func2<Address, List<Transaction>, TransactionList>() {
              @Override
              public TransactionList call(Address address, List<Transaction> transactions) {
                return new TransactionList(address, transactions);
              }
            });

    subscribe(observable);
  }
}
