package de.tum.in.securebitcoinwallet;

import android.app.Application;
import com.hannesdorfmann.mosby.dagger1.Injector;
import dagger.ObjectGraph;
import de.tum.in.securebitcoinwallet.dagger.ApplicationModule;

/**
 * @author Hannes Dorfmann
 */
public class SecureWalletApplication extends Application implements Injector{

  ObjectGraph objectGraph;

  @Override public void onCreate() {
    super.onCreate();
    objectGraph = ObjectGraph.create(new ApplicationModule(this));
  }

  @Override public ObjectGraph getObjectGraph() {
    return null;
  }
}
