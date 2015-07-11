package de.tum.in.securebitcoinwallet.smartcard.exception;

/**
 * Smartcard is not connected.
 *
 * @author Benedikt Schlagberger
 */
public class SmartcardNotConnectedException extends SmartcardException {
  public SmartcardNotConnectedException(String s) {
    super(s);
  }

  public SmartcardNotConnectedException() {

  }
}
