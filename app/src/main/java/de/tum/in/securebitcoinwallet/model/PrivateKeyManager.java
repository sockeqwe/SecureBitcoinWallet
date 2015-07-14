package de.tum.in.securebitcoinwallet.model;

import java.io.File;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import rx.Observable;
import rx.Subscriber;

/**
 * This class is responsible to manage and access the private keys on the secure element.
 *
 * @author Hannes Dorfmann
 */
public interface PrivateKeyManager {

  /**
   * Checks whether the card has been initialized yet.
   */
  public Observable<Boolean> isCardInitialized();

  /**
   * Get the encrypted private key from secure sd card
   *
   * @param pin the card's PIN
   * @param address The address you want to get the private key for
   * @return The encrypted private key as a byte array
   */
  public Observable<byte[]> getEncryptedPrivateKey(byte[] pin, String address);

  /**
   * Gets the remaining space in key slots from the card.
   *
   * @param pin the card's PIN
   * @return The amount of free slots on the card
   */
  public Observable<Integer> getRemainingSlots(byte[] pin);

  /**
   * Change the PIN
   *
   * @param pin the card's PIN
   * @param newPin the new PIN
   * @return nothing, {@link Subscriber#onCompleted()} or {@link Subscriber#onError(Throwable)} will
   * be called
   */
  public Observable<Void> changePin(byte[] pin, byte[] newPin);

  /**
   * If the smartcard is locked, this method can be used to unlock it with the PUK.
   *
   * @param puk The PUK from the card's setup
   * @return nothing, {@link Subscriber#onCompleted()} or {@link Subscriber#onError(Throwable)} will
   * be called
   */
  public Observable<Void> unlockSmartcard(byte[] puk, byte[] newPin);

  /**
   * Adds a raw private key to the secure storage.
   *
   * @param pin the card's PIN
   * @param keyFile Keyfile conatining the key pair to import
   * @return nothing, {@link Subscriber#onCompleted()} or {@link Subscriber#onError(Throwable)} will
   * be called
   */
  public Observable<Void> addPrivateKey(byte[] pin, File keyFile);

  /**
   * Generates a new key pair on the card. The private key is stored on the card, the public one is
   * returned.
   *
   * @param pin the card's PIN
   * @return The generated public key. The private key is stored on the card.
   */
  public Observable<byte[]> generateNewKey(byte[] pin);

  /**
   * Removes an address
   *
   * @param pin the card's PIN
   * @param address the address to delete
   * @return nothing, {@link Subscriber#onCompleted()} or {@link Subscriber#onError(Throwable)} will
   * be called
   */
  public Observable<Void> removePrivateKeyForAddress(byte[] pin, String address);

  /**
   * Signs the giben hash with the private key specified by the Bitcoin address.
   *
   * @param pin the card's PIN
   * @param address The Bitcoin address specifying the private key to use for signing
   * @param sha256hash The hash which should be signed.
   * @return The signature as a byte array.
   */
  public Observable<byte[]> signSHA256Hash(byte[] pin, String address, byte[] sha256hash);
}
