package de.tum.in.securebitcoinwallet.dagger;

import android.content.Context;
import com.hannesdorfmann.sqlbrite.dao.DaoManager;
import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import de.tum.in.securebitcoinwallet.IntentStarter;
import de.tum.in.securebitcoinwallet.accounts.AccountListActivity;
import de.tum.in.securebitcoinwallet.accounts.AccountListAdapter;
import de.tum.in.securebitcoinwallet.accounts.AccountListFragment;
import de.tum.in.securebitcoinwallet.accounts.AccountListPresenter;
import de.tum.in.securebitcoinwallet.common.BaseActivity;
import de.tum.in.securebitcoinwallet.common.ErrorMessageDeterminer;
import de.tum.in.securebitcoinwallet.common.RecyclerViewFragment;
import de.tum.in.securebitcoinwallet.lock.LockFragment;
import de.tum.in.securebitcoinwallet.lock.LockPresenter;
import de.tum.in.securebitcoinwallet.model.CurrencyManager;
import de.tum.in.securebitcoinwallet.model.PrivateKeyManager;
import de.tum.in.securebitcoinwallet.model.TransactionManager;
import de.tum.in.securebitcoinwallet.model.WalletManager;
import de.tum.in.securebitcoinwallet.model.database.AddressDao;
import de.tum.in.securebitcoinwallet.model.mock.MockPrivateKeyManager;
import de.tum.in.securebitcoinwallet.model.mock.MockTransactionManager;
import de.tum.in.securebitcoinwallet.model.mock.MockWalletManager;
import de.tum.in.securebitcoinwallet.transactions.TransactionsActivity;
import de.tum.in.securebitcoinwallet.transactions.TransactionsAdapter;
import de.tum.in.securebitcoinwallet.transactions.TransactionsFragment;
import de.tum.in.securebitcoinwallet.transactions.TransactionsPresenter;
import javax.inject.Singleton;

/**
 * @author Hannes Dorfmann
 */
@Module(

    injects = {
        BaseActivity.class, RecyclerViewFragment.class, AccountListActivity.class,
        LockFragment.class, LockPresenter.class, AccountListFragment.class,
        AccountListPresenter.class, AccountListAdapter.class, TransactionsPresenter.class,
        TransactionsFragment.class, TransactionsAdapter.class, TransactionsActivity.class,
    },
    library = true,
    complete = false // TODO remove this

) public class ApplicationModule {

  private Context context;
  private PrivateKeyManager privateKeyManager;
  private AddressDao walletDao;

  public ApplicationModule(Context context) {
    this.context = context;
    privateKeyManager = new MockPrivateKeyManager();
    walletDao = new AddressDao();
    new DaoManager(context, "wallet.db", 1, walletDao);
  }

  @Provides @Singleton public PrivateKeyManager providesPrivateKeyManager() {
    return privateKeyManager;
  }

  @Provides @Singleton public TransactionManager provideTransactionManager() {
    return new MockTransactionManager();
  }

  @Provides @Singleton public WalletManager provideWalletManager(PrivateKeyManager keyManager) {
    return new MockWalletManager(keyManager);
  }

  @Provides @Singleton public EventBus provideEventBus() {
    return EventBus.getDefault();
  }

  @Provides @Singleton public CurrencyManager provideCurrencyManager() {
    return new CurrencyManager();
  }

  @Provides @Singleton public ErrorMessageDeterminer provideErrorMessageDeterminer() {
    return new ErrorMessageDeterminer(context);
  }

  @Provides @Singleton public IntentStarter provideIntentStarter() {
    return new IntentStarter();
  }

  @Provides @Singleton AddressDao provideDao() {

    return walletDao;
  }
}
