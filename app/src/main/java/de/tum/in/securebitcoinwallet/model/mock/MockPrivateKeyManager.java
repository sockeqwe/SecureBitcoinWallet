package de.tum.in.securebitcoinwallet.model.mock;

import de.tum.in.securebitcoinwallet.model.PrivateKeyManager;
import de.tum.in.securebitcoinwallet.model.exception.NotFoundException;
import de.tum.in.securebitcoinwallet.smartcard.exception.AppletAlreadyInitializedException;
import de.tum.in.securebitcoinwallet.smartcard.exception.KeyStoreFullException;
import de.tum.in.securebitcoinwallet.util.BitcoinUtils;
import java.io.File;
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
  private final static boolean isInitialized = false;

  /**
   * Map from address to private key
   */
  private Map<String, byte[]> privateKeyMap = new HashMap<>();
  private int remainingSlots = MAX_SLOTS;

  public MockPrivateKeyManager() {
    // generate sample data
    for (int i = 0; i < 10; i++) {
      try {
        addRandomKey();
      } catch (KeyStoreFullException e) {
        // Store is full, stop generating new addresses
        break;
      }
    }
  }

  @Override public Observable<byte[]> setup() {
    return Observable.defer(new Func0<Observable<byte[]>>() {
      @Override public Observable<byte[]> call() {
        if (isInitialized) {
          return Observable.error(new AppletAlreadyInitializedException());
        }

        byte[] puk = new byte[8];
        new Random().nextBytes(puk);

        return Observable.just(puk);
      }
    });
  }

  @Override public Observable<Boolean> isCardInitialized() {
    return Observable.defer(new Func0<Observable<Boolean>>() {
      @Override public Observable<Boolean> call() {
        return Observable.just(isInitialized);
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
        try {
          addRandomKey();
          return Observable.empty();
        } catch (KeyStoreFullException e) {
          return Observable.error(e);
        }
      }
    });
  }

  @Override public Observable<byte[]> generateNewKey(byte[] pin) {
    return Observable.defer(new Func0<Observable<byte[]>>() {
      @Override public Observable<byte[]> call() {
        try {
          return Observable.just(addRandomKey());
        } catch (KeyStoreFullException e) {
          return Observable.error(e);
        }
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

  /**
   * Adds a mocked private key to the privateKeyMap and returns a public key.
   */
  private byte[] addRandomKey() throws KeyStoreFullException {
    if (remainingSlots == 0) {
      throw new KeyStoreFullException();
    }

    byte[] publicKey = new byte[65];
    new Random().nextBytes(publicKey);
    publicKey[0] = 4;

    byte[] privateKey = new byte[32];
    new Random().nextBytes(privateKey);

    privateKeyMap.put(BitcoinUtils.calculateBitcoinAddress(publicKey), privateKey);
    remainingSlots--;

    return publicKey;
  }
}
