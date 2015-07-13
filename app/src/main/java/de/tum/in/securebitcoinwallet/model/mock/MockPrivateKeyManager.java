package de.tum.in.securebitcoinwallet.model.mock;

import android.content.Context;
import de.tum.in.securebitcoinwallet.model.PrivateKeyManager;
import de.tum.in.securebitcoinwallet.model.exception.IncorrectPinException;
import de.tum.in.securebitcoinwallet.model.exception.NotFoundException;
import de.tum.in.securebitcoinwallet.smartcard.exception.SmartCardException;
import java.io.File;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import rx.Observable;
import rx.functions.Func0;

/**
 * Simple mock implementation of {@link PrivateKeyManager}
 *
 * @author Hannes Dorfmann
 * @deprecated Implementation is missing functionality.
 */
@Deprecated public class MockPrivateKeyManager implements PrivateKeyManager {
  private final static int MAX_SLOTS = 15;

  private final Context context;

  /**
   * Map from address to private key
   */
  private Map<String, byte[]> privateKeyMap = new HashMap<>();
  private int remainingSlots = MAX_SLOTS;

  public MockPrivateKeyManager(Context context) {
    this.context = context;

    // generate sample data
    for (int i = 0; i < 10; i++) {
      privateKeyMap.put("address" + i, ("privateKey" + i).getBytes());
      remainingSlots--;
    }
  }

  @Override public Observable<Boolean> isCardInitialized() {
    return Observable.defer(new Func0<Observable<Boolean>>() {
      @Override public Observable<Boolean> call() {
        return Observable.just(false);
      }
    });
  }

  @Override public Observable<byte[]> getEncryptedPrivateKey(byte[] pin, final String address) {
    return Observable.defer(new Func0<Observable<byte[]>>() {
      @Override public Observable<byte[]> call() {
        return Observable.just(privateKeyMap.get(address));
      }
    });
  }

  @Override public Observable<Integer> getRemainingSlots(byte[] pin) {
    return Observable.defer(new Func0<Observable<Integer>>() {
      @Override public Observable<Integer> call() {
        return Observable.just(remainingSlots);
      }
    });
  }

  @Override public Observable<Void> changePin(byte[] pin, byte[] newPin) {
    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {
        return Observable.empty();
      }
    });
  }

  @Override public Observable<Void> unlockSmartcard(byte[] puk, byte[] newPin) {
    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {
        return Observable.empty();
      }
    });
  }

  @Override public Observable<Void> addPrivateKey(byte[] pin, File keyFile) {
    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {

        return Observable.empty();
      }
    });
  }

  @Override public Observable<ECPublicKey> generateNewKey(byte[] pin) {
    return Observable.defer(new Func0<Observable<ECPublicKey>>() {
      @Override public Observable<ECPublicKey> call() {
        return Observable.error(new SmartCardException("Could not generate new key!"));
      }
    });
  }

  @Override public Observable<Void> removePrivateKeyForAddress(byte[] pin, final String address) {
    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {
        if (privateKeyMap.containsKey(address)) {
          privateKeyMap.remove(address);
          remainingSlots--;
          return Observable.empty();
        } else {
          return Observable.error(new NotFoundException());
        }
      }
    });
  }

  @Override
  public Observable<byte[]> signSHA256Hash(byte[] pin, final String address, byte[] sha256hash) {
    return Observable.defer(new Func0<Observable<byte[]>>() {
      @Override public Observable<byte[]> call() {
        if (privateKeyMap.containsKey(address)) {
          byte[] fakeSignature = new byte[70];

          new Random().nextBytes(fakeSignature);

          return Observable.just(fakeSignature);
        } else {
          return Observable.error(new NotFoundException());
        }
      }
    });
  }
}
