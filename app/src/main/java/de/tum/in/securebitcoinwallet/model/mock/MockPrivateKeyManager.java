package de.tum.in.securebitcoinwallet.model.mock;

import de.tum.in.securebitcoinwallet.model.PrivateKeyManager;
import de.tum.in.securebitcoinwallet.model.exception.NotFoundException;
import de.tum.in.securebitcoinwallet.smartcard.exception.AppletAlreadyInitializedException;
import de.tum.in.securebitcoinwallet.smartcard.exception.AuthenticationFailedExeption;
import de.tum.in.securebitcoinwallet.smartcard.exception.CardLockedException;
import de.tum.in.securebitcoinwallet.smartcard.exception.KeyAlreadyInStoreException;
import de.tum.in.securebitcoinwallet.smartcard.exception.KeyStoreFullException;
import de.tum.in.securebitcoinwallet.smartcard.exception.SmartCardException;
import de.tum.in.securebitcoinwallet.util.BitcoinUtils;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import rx.Observable;
import rx.functions.Func0;

/**
 * Simple mock implementation of {@link PrivateKeyManager}
 *
 * @author Hannes Dorfmann
 */
public class MockPrivateKeyManager implements PrivateKeyManager {
  private final static int MAX_SLOTS = 15;
  private final static int MAX_PIN_RETRIES = 3;

  private int remainingRetries = MAX_PIN_RETRIES;
  private boolean isInitialized = false;

  private byte[] puk;
  private byte[] pin = { 1, 2, 3, 4 };

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
        isInitialized = true;
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

  @Override
  public Observable<byte[]> getEncryptedPrivateKey(final byte[] pin, final String address) {
    return Observable.defer(new Func0<Observable<byte[]>>() {
      @Override public Observable<byte[]> call() {
        try {
          checkPIN(pin);
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
        return Observable.just(privateKeyMap.get(address));
      }
    });
  }

  @Override public Observable<Integer> getRemainingSlots(final byte[] pin) {
    return Observable.defer(new Func0<Observable<Integer>>() {
      @Override public Observable<Integer> call() {
        try {
          checkPIN(pin);
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
        return Observable.just(remainingSlots);
      }
    });
  }

  @Override public Observable<Void> changePin(final byte[] pin, final byte[] newPin) {
    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {
        try {
          checkPIN(pin);
        } catch (SmartCardException e) {
          return Observable.error(e);
        }

        if (pin.length < 4 || pin.length > 8) {
          return Observable.error(
              new RuntimeException("PIN has wrong length. May only be between 4 and 8 characters"));
        }
        MockPrivateKeyManager.this.pin = pin;
        return Observable.empty();
      }
    });
  }

  @Override public Observable<Void> unlockSmartcard(final byte[] puk, final byte[] newPin) {
    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {
        if (!Arrays.equals(puk, MockPrivateKeyManager.this.puk)) {
          return Observable.error(new AuthenticationFailedExeption());
        }

        if (newPin.length < 4 || newPin.length > 8) {
          return Observable.error(
              new RuntimeException("PIN has wrong length. May only be between 4 and 8 characters"));
        }

        MockPrivateKeyManager.this.pin = newPin;
        remainingRetries = MAX_PIN_RETRIES;

        return Observable.empty();
      }
    });
  }

  @Override public Observable<Void> addPrivateKey(final byte[] pin, File keyFile) {
    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {
        try {
          try {
            checkPIN(pin);
          } catch (SmartCardException e) {
            return Observable.error(e);
          }
          addRandomKey();
          return Observable.empty();
        } catch (KeyStoreFullException e) {
          return Observable.error(e);
        }
      }
    });
  }

  @Override
  public Observable<Void> importEncryptedPrivateKey(final byte[] pin, final String bitcoinAddress,
      final byte[] encryptedPrivateKey) {
    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {
        try {
          checkPIN(pin);
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
        if (remainingSlots == 0) {
          return Observable.error(new KeyStoreFullException());
        }

        if (privateKeyMap.containsKey(bitcoinAddress)) {
          return Observable.error(new KeyAlreadyInStoreException());
        }

        if (encryptedPrivateKey.length != 32) {
          return Observable.error(new RuntimeException("Private key has wrong length!"));
        }

        privateKeyMap.put(bitcoinAddress, encryptedPrivateKey);
        return Observable.empty();
      }
    });
  }

  @Override public Observable<byte[]> generateNewKey(final byte[] pin) {
    return Observable.defer(new Func0<Observable<byte[]>>() {
      @Override public Observable<byte[]> call() {
        try {
          checkPIN(pin);
          return Observable.just(addRandomKey());
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
      }
    });
  }

  @Override
  public Observable<Void> removePrivateKeyForAddress(final byte[] pin, final String address) {
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

  @Override public Observable<byte[]> signSHA256Hash(final byte[] pin, final String address,
      byte[] sha256hash) {
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

  /**
   * Checks the given PIN
   */
  private void checkPIN(byte[] pin) throws SmartCardException {
    if (remainingRetries == 0) {
      throw new CardLockedException();
    }
    if (!Arrays.equals(pin, this.pin)) {
      remainingRetries--;
      throw new AuthenticationFailedExeption();
    }
    remainingRetries = MAX_PIN_RETRIES;
  }
}
