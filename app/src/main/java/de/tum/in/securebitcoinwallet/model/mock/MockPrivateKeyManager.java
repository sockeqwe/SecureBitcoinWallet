package de.tum.in.securebitcoinwallet.model.mock;

import de.tum.in.securebitcoinwallet.model.PrivateKeyManager;
import rx.Observable;

/**
 * @author Hannes Dorfmann
 */
public class MockPrivateKeyManager implements PrivateKeyManager {

  @Override public Observable<String> getPrivateKey(String pin, String address) {
    return null;
  }

  @Override public Observable<Boolean> isPinSet() {
    return null;
  }

  @Override public Observable<Void> changePin(String oldPin, String newPin) {
    return null;
  }

  @Override public Observable<Void> addAddress(String pin, String privateKey, String address) {
    return null;
  }

  @Override public Observable<Void> removeAddress(String pin, String address) {
    return null;
  }
}
