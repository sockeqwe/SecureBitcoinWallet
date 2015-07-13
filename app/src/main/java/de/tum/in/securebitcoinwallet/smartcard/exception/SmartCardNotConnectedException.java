package de.tum.in.securebitcoinwallet.smartcard.exception;

/**
 * Smartcard is not connected.
 *
 * @author Benedikt Schlagberger
 */
public class SmartCardNotConnectedException extends SmartCardException {
  public SmartCardNotConnectedException(String s) {
    super(s);
  }

  public SmartCardNotConnectedException() {

  }
}
