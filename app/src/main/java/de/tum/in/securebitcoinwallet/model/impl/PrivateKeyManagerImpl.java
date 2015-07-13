package de.tum.in.securebitcoinwallet.model.impl;

import android.content.Context;

import java.io.File;
import java.security.KeyPair;
import java.security.interfaces.ECPublicKey;

import javax.inject.Inject;

import de.tum.in.securebitcoinwallet.model.PrivateKeyManager;
import de.tum.in.securebitcoinwallet.smartcard.SmartCardManager;
import de.tum.in.securebitcoinwallet.smartcard.exception.SmartCardException;
import de.tum.in.securebitcoinwallet.util.BitcoinUtils;
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
   * The {@link SmartCardManager} of this PrivateKeyManager. TODO has to be initialized with
   * context!
   */
  @Inject private SmartCardManager smartCardManager;

  /**
   * The {@link Context} of this manager.
   */
  private Context context;

  @Override public Observable<byte[]> getEncryptedPrivateKey(final String address) {
    return Observable.defer(new Func0<Observable<byte[]>>() {
      @Override public Observable<byte[]> call() {
        try {
          return Observable.just(smartCardManager.exportEncryptedPrivateKey(address));
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
      }
    });
  }

  @Override public Observable<Integer> getRemainingSlots() {
    return Observable.defer(new Func0<Observable<Integer>>() {
      @Override public Observable<Integer> call() {
        try {
          return Observable.just(smartCardManager.getFreeSlots());
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
      }
    });
  }

  @Override public Observable<Void> changePin(final byte[] newPin) {
    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {
        try {
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

  @Override public Observable<Void> addPrivateKey(final File keyFile) {
    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {
        KeyPair keyPair = BitcoinUtils.getKeyPairOfFile(keyFile);

        try {
          smartCardManager.importKey(keyPair);
          return Observable.empty();
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
      }
    });
  }

  @Override public Observable<ECPublicKey> generateNewKey() {
    return Observable.defer(new Func0<Observable<ECPublicKey>>() {
      @Override public Observable<ECPublicKey> call() {
        try {
          return Observable.just(smartCardManager.generateNewKey());
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
      }
    });
  }

  @Override public Observable<Void> removePrivateKeyForAddress(final String address) {
    return Observable.defer(new Func0<Observable<Void>>() {
      @Override public Observable<Void> call() {
        try {
          smartCardManager.deleteKey(address);
          return Observable.empty();
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
      }
    });
  }

  @Override
  public Observable<byte[]> signSHA256Hash(final String address, final byte[] sha256hash) {
    return Observable.defer(new Func0<Observable<byte[]>>() {
      @Override public Observable<byte[]> call() {
        try {
          return Observable.just(smartCardManager.signSHA256Hash(sha256hash, address));
        } catch (SmartCardException e) {
          return Observable.error(e);
        }
      }
    });
  }
}
