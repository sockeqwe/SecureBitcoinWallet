package de.tum.in.securebitcoinwallet.smartcard.exception;

/**
 * Signals errors during communication with the smartcard.
 *
 * @author Benedikt Schlagberger
 */
public class SmartcardException extends Exception {

  /**
   * {@inheritDoc}
   */
  public SmartcardException(String detailMessage) {
    super(detailMessage);
  }

  /**
   * {@inheritDoc}
   */
  public SmartcardException() {

  }
}
