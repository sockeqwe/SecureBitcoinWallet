package de.tum.in.securebitcoinwallet.model;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import rx.Observable;
import rx.Subscriber;

/**
 * This class is responsible to manage and access the private keys on the secure element.
 *
 * @author Hannes Dorfmann
 */
public interface PrivateKeyManager {

  /**
   * Get the encrypted private key from secure sd card
   *
   * @param address The address you want to get the private key for
   * @return The encrypted private key as a byte array
   */
  public Observable<byte[]> getEncryptedPrivateKey(String address);

  /**
   * Gets the remainign space in key slots from the card.
   *
   * @return The amount of free slots on the card
   */
  public Observable<Integer> getRemainingSlots();

  /**
   * Change the PIN
   *
   * @param newPin the new PIN
   * @return nothing, {@link Subscriber#onCompleted()} or {@link Subscriber#onError(Throwable)} will
   * be called
   */
  public Observable<Void> changePin(byte[] newPin);

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
   * @param privateKey the new private key
   * @return nothing, {@link Subscriber#onCompleted()} or {@link Subscriber#onError(Throwable)} will
   * be called
   */
  public Observable<Void> addPrivateKey(ECPrivateKey privateKey);

  /**
   * Generates a new key pair on the card. The private key is stored on the card, the public one is
   * returned.
   *
   * @return The generated public key. The private key is stored on the card.
   */
  public Observable<ECPublicKey> generateNewKey();

  /**
   * Removes an address
   *
   * @param address the address to delete
   * @return nothing, {@link Subscriber#onCompleted()} or {@link Subscriber#onError(Throwable)} will
   * be called
   */
  public Observable<Void> removePrivateKeyForAddress(String address);

  /**
   * Signs the giben hash with the private key specified by the Bitcoin address.
   *
   * @param address The Bitcoin address specifying the private key to use for signing
   * @param sha256hash The hash which should be signed.
   * @return The signature as a byte array.
   */
  public Observable<byte[]> signSHA256Hash(String address, byte[] sha256hash);
}
