package de.tum.in.securebitcoinwallet.dagger;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import de.tum.in.securebitcoinwallet.BaseActivity;
import de.tum.in.securebitcoinwallet.model.PrivateKeyManager;
import de.tum.in.securebitcoinwallet.model.TransactionManager;
import de.tum.in.securebitcoinwallet.model.WalletManager;
import de.tum.in.securebitcoinwallet.model.mock.MockPrivateKeyManager;
import de.tum.in.securebitcoinwallet.model.mock.MockTransactionManager;
import de.tum.in.securebitcoinwallet.model.mock.MockWalletManager;
import javax.inject.Singleton;

/**
 * @author Hannes Dorfmann
 */
@Module(

    injects = {
        BaseActivity.class
    },
    library =  true,
    complete =  false // TODO remove this

) public class ApplicationModule {

  private Context context;
  private PrivateKeyManager privateKeyManager;

  public ApplicationModule(Context context) {
    this.context = context;
    privateKeyManager = new MockPrivateKeyManager();
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
}
