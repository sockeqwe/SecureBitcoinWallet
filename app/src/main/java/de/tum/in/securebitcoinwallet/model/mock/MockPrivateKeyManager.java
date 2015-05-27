package de.tum.in.securebitcoinwallet.model.mock;

import de.tum.in.securebitcoinwallet.model.PrivateKeyManager;
import de.tum.in.securebitcoinwallet.model.exception.IncorrectPinException;
import de.tum.in.securebitcoinwallet.model.exception.NotFoundException;
import java.util.HashMap;
import java.util.Map;
import rx.Observable;
import rx.functions.Func0;

/**
 * Simple mock implementation of {@link PrivateKeyManager}
 * @author Hannes Dorfmann
 */
public class MockPrivateKeyManager implements PrivateKeyManager {

  /**
   * Map from address to private key
   */
  private Map<String, String> privateKeyMap = new HashMap<>();
  private String PIN = "1234";

  public MockPrivateKeyManager() {

    // generate sample data
    for (int i = 0; i < 10; i++) {
      privateKeyMap.put("address" + i, "privateKey" + i);
    }
  }

  private boolean isPinCorrect(String pin) {
    return this.PIN.equals(pin);
  }

  @Override public Observable<String> getPrivateKey(final String pin, final String address) {
    return Observable.defer(new Func0<Observable<String>>() {
      @Override public Observable<String> call() {

        if (!isPinCorrect(pin)) {
          return Observable.error(new IncorrectPinException());
        }

        return Observable.just(privateKeyMap.get(address));
      }
    });
  }

  @Override public Observable<Boolean> isPinSet() {
    return Observable.just(true);
  }

  @Override public Observable<Void> changePin(final String oldPin, final String newPin) {

    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {

        if (!isPinCorrect(oldPin)) {
          return Observable.error(new IncorrectPinException());
        }

        PIN = newPin;

        return Observable.empty();
      }
    });
  }

  @Override public Observable<Void> addAddress(final String pin, final String privateKey,
      final String address) {
    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {

        if (!isPinCorrect(pin)) {
          return Observable.error(new IncorrectPinException());
        }

        privateKeyMap.put(privateKey, address);

        return Observable.empty();
      }
    });
  }

  @Override public Observable<Void> removeAddress(final String pin, final String address) {

    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {

        if (!isPinCorrect(pin)) {
          return Observable.error(new IncorrectPinException());
        }

        String key = privateKeyMap.remove(address);
        if (key == null) {
          return Observable.error(new NotFoundException());
        }

        return Observable.empty();
      }
    });
  }
}
