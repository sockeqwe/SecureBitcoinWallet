package de.tum.in.securebitcoinwallet.model.impl;

import de.tum.in.securebitcoinwallet.model.PrivateKeyManager;
import de.tum.in.securebitcoinwallet.smartcard.SmartCardManager;
import de.tum.in.securebitcoinwallet.smartcard.exception.SmartCardException;
import de.tum.in.securebitcoinwallet.util.BitcoinUtils;
import java.io.File;
import java.security.KeyPair;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import rx.Observable;
import rx.functions.Func0;

/**
 * Implementation of {@link PrivateKeyManager}. Uses {@link SmartCardManager} to communicate with
 * the secure element.
 *
 * @author Benedikt Schlagberger
 */
public class PrivateKeyManagerImpl implements PrivateKeyManager {

  /**
   * The {@link SmartCardManager} of this PrivateKeyManager.
   */
  private SmartCardManager smartCardManager;

  @Override public Observable<Boolean> isCardInitialized() {
    return Observable.defer(new Func0<Observable<Boolean>>() {
      @Override public Observable<Boolean> call() {
        try {
          return Observable.just(smartCardManager.isAppletInitialized());
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
      }
    });
  }

  @Override
  public Observable<byte[]> getEncryptedPrivateKey(final byte[] pin, final String address) {
    return Observable.defer(new Func0<Observable<byte[]>>() {
      @Override public Observable<byte[]> call() {
        try {
          smartCardManager.authenticate(pin);
          return Observable.just(smartCardManager.exportEncryptedPrivateKey(address));
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
      }
    });
  }

  @Override public Observable<Integer> getRemainingSlots(final byte[] pin) {
    return Observable.defer(new Func0<Observable<Integer>>() {
      @Override public Observable<Integer> call() {
        try {
          smartCardManager.authenticate(pin);
          return Observable.just(smartCardManager.getFreeSlots());
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
      }
    });
  }

  @Override public Observable<Void> changePin(final byte[] pin, final byte[] newPin) {
    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {
        try {
          smartCardManager.authenticate(pin);
          smartCardManager.changePIN(newPin);
          return Observable.empty();
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
      }
    });
  }

  @Override public Observable<Void> unlockSmartcard(final byte[] puk, final byte[] newPin) {
    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {
        try {
          smartCardManager.unlock(puk, newPin);
          return Observable.empty();
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
      }
    });
  }

  @Override public Observable<Void> addPrivateKey(final byte[] pin, final File keyFile) {
    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {
        KeyPair keyPair = BitcoinUtils.getKeyPairOfFile(keyFile);
        try {
          smartCardManager.authenticate(pin);
          smartCardManager.importKey(keyPair);
          return Observable.empty();
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
      }
    });
  }

  @Override public Observable<ECPublicKey> generateNewKey(final byte[] pin) {
    return Observable.defer(new Func0<Observable<ECPublicKey>>() {
      @Override public Observable<ECPublicKey> call() {
        try {
          smartCardManager.authenticate(pin);
          return Observable.just(smartCardManager.generateNewKey());
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
        try {
          smartCardManager.authenticate(pin);
          smartCardManager.deleteKey(address);
          return Observable.empty();
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
      }
    });
  }

  @Override public Observable<byte[]> signSHA256Hash(final byte[] pin, final String address,
      final byte[] sha256hash) {
    return Observable.defer(new Func0<Observable<byte[]>>() {
      @Override public Observable<byte[]> call() {
        try {
          smartCardManager.authenticate(pin);
          return Observable.just(smartCardManager.signSHA256Hash(sha256hash, address));
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
      }
    });
  }
}
