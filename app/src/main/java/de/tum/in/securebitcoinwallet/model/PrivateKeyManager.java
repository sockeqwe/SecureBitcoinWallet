package de.tum.in.securebitcoinwallet.model;

import rx.Observable;
import rx.Subscriber;

/**
 * This class is responsible to manage and access the private key from secure element.
 *
 * @author Hannes Dorfmann
 */
public interface PrivateKeyManager {

  /**
   * Get the private key from secure sd card
   *
   * @param pin The secure pin to access
   * @param address The address you want to get the private key for
   * @return The private key as String
   */
  public Observable<String> getPrivateKey(String pin, String address);

  /**
   * Is a PIN set?
   *
   * @return true if sd card is protected with a PIN, otherwise false
   */
  public Observable<Boolean> isPinSet();

  /**
   * Change the PIN
   *
   * @param oldPin the old PIN
   * @param newPin the new PIN
   * @return nothing, {@link Subscriber#onCompleted()} or {@link Subscriber#onError(Throwable)} will
   * be called
   */
  public Observable<Void> changePin(String oldPin, String newPin);

  /**
   * Adds an address / private key and address to the secucre storage
   *
   * @param pin The PIN
   * @param privateKey the new private key
   * @param address the Address
   * @return nothing, {@link Subscriber#onCompleted()} or {@link Subscriber#onError(Throwable)} will
   * be called
   */
  public Observable<Void> addAddress(String pin, String privateKey, String address);

  /**
   * Removes  an address
   *
   * @param pin The PIN
   * @param address the address to delete
   * @return nothing, {@link Subscriber#onCompleted()} or {@link Subscriber#onError(Throwable)} will
   * be called
   */
  public Observable<Void> removeAddress(String pin, String address);

  /**
   * Checks if the pin is correct
   *
   * @param pin The pin to check
   * @return nothing, {@link Subscriber#onCompleted()} or {@link Subscriber#onError(Throwable)} will
   * be called
   */
  public Observable<Void> checkPin(String pin);
}
