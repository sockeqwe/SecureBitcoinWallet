package de.tum.in.securebitcoinwallet.smartcard.exception;

/**
 * Signals errors during communication with the smartcard.
 *
 * @author Benedikt Schlagberger
 */
public class SmartCardException extends Exception {

  /**
   * {@inheritDoc}
   */
  public SmartCardException(String detailMessage) {
    super(detailMessage);
  }

  /**
   * {@inheritDoc}
   */
  public SmartCardException() {

  }
}
