package de.tum.in.securebitcoinwallet.dagger;

import android.content.Context;
import com.hannesdorfmann.sqlbrite.dao.DaoManager;
import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import de.tum.in.securebitcoinwallet.BuildConfig;
import de.tum.in.securebitcoinwallet.IntentStarter;
import de.tum.in.securebitcoinwallet.addresses.AddressListActivity;
import de.tum.in.securebitcoinwallet.addresses.AddressListAdapter;
import de.tum.in.securebitcoinwallet.addresses.AddressListFragment;
import de.tum.in.securebitcoinwallet.addresses.AddressListPresenter;
import de.tum.in.securebitcoinwallet.addresses.create.CreateAddressActivity;
import de.tum.in.securebitcoinwallet.addresses.create.nameinput.NameInputPresenter;
import de.tum.in.securebitcoinwallet.addresses.create.pininput.PinInputFragment;
import de.tum.in.securebitcoinwallet.addresses.create.pininput.PinInputPresenter;
import de.tum.in.securebitcoinwallet.common.BaseActivity;
import de.tum.in.securebitcoinwallet.common.ErrorMessageDeterminer;
import de.tum.in.securebitcoinwallet.common.RecyclerViewFragment;
import de.tum.in.securebitcoinwallet.lock.LockFragment;
import de.tum.in.securebitcoinwallet.model.BackendApi;
import de.tum.in.securebitcoinwallet.model.sync.BitcoinSync;
import de.tum.in.securebitcoinwallet.model.CurrencyManager;
import de.tum.in.securebitcoinwallet.model.PrivateKeyManager;
import de.tum.in.securebitcoinwallet.model.WalletManager;
import de.tum.in.securebitcoinwallet.model.database.AddressDao;
import de.tum.in.securebitcoinwallet.model.database.TransactionDao;
import de.tum.in.securebitcoinwallet.model.impl.WalletManagerImpl;
import de.tum.in.securebitcoinwallet.model.mock.MockPrivateKeyManager;
import de.tum.in.securebitcoinwallet.smartcard.SmartCardManager;
import de.tum.in.securebitcoinwallet.transactions.TransactionsActivity;
import de.tum.in.securebitcoinwallet.transactions.TransactionsAdapter;
import de.tum.in.securebitcoinwallet.transactions.TransactionsFragment;
import de.tum.in.securebitcoinwallet.transactions.TransactionsPresenter;
import de.tum.in.securebitcoinwallet.transactions.create.CreateTransactionActivity;
import de.tum.in.securebitcoinwallet.transactions.create.transactioninput.TransactionInputFragment;
import de.tum.in.securebitcoinwallet.transactions.create.transactionpininput.TransactionPinInputFragment;
import de.tum.in.securebitcoinwallet.transactions.create.transactionpininput.TransactionPinInputPresenter;
import de.tum.in.securebitcoinwallet.util.LoganSquareConverter;
import javax.inject.Singleton;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * @author Hannes Dorfmann
 */
@Module(

    injects = {
        BaseActivity.class, RecyclerViewFragment.class, AddressListActivity.class,
        LockFragment.class, AddressListFragment.class, AddressListPresenter.class,
        AddressListAdapter.class, TransactionsPresenter.class, TransactionsFragment.class,
        TransactionsAdapter.class, TransactionsActivity.class, BitcoinSync.class,
        CreateAddressActivity.class, NameInputPresenter.class, PinInputPresenter.class,
        PinInputFragment.class, CreateTransactionActivity.class, TransactionInputFragment.class,
        TransactionPinInputFragment.class, TransactionPinInputPresenter.class,
    },
    library = true,
    complete = false // TODO remove this

) public class ApplicationModule {

  private Context context;
  private AddressDao addressDao;
  private TransactionDao transactionDao;

  public ApplicationModule(Context context) {
    this.context = context;

    addressDao = new AddressDao();
    transactionDao = new TransactionDao();
    new DaoManager(context, "wallet.db", 1, addressDao, transactionDao);
  }

  @Provides @Singleton
  public PrivateKeyManager providesPrivateKeyManager(SmartCardManager manager) {
    return new MockPrivateKeyManager(); // TODO replace with right one
  }

  @Provides @Singleton
  public WalletManager provideWalletManager(PrivateKeyManager keyManager, AddressDao addressDao,
      TransactionDao transactionDao) {
    return new WalletManagerImpl(keyManager, addressDao, transactionDao);
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

  @Provides @Singleton AddressDao provideAddressDao() {
    return addressDao;
  }

  @Provides @Singleton TransactionDao provideTranasactionDao() {
    return transactionDao;
  }

  @Provides @Singleton BackendApi provideBackendApi(RestAdapter adapter) {
    return adapter.create(BackendApi.class);
  }

  @Provides @Singleton RestAdapter provideRestAdapter() {
    RestAdapter adapter = new RestAdapter.Builder().setEndpoint("https://api.github.com")
        .setClient(new OkClient())
        .setConverter(new LoganSquareConverter())
        .build();
    adapter.setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE);
    return adapter;
  }

  @Provides @Singleton BitcoinSync provideBitcoinSync(BackendApi backendApi, AddressDao addressDao,
      TransactionDao transactionDao) {
    return new BitcoinSync(backendApi, addressDao, transactionDao);
  }

  @Singleton @Provides public SmartCardManager provideSmartCardManager() {
    return new SmartCardManager(context);
  }
}
