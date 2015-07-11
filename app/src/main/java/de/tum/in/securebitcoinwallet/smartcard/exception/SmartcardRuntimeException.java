package de.tum.in.securebitcoinwallet.smartcard.exception;

/**
 * Runtime exception for unexpected errors.
 *
 * @author Benedikt Schlagberger
 */
public class SmartcardRuntimeException extends RuntimeException {
  public SmartcardRuntimeException(String s) {
    super(s);
  }
}
