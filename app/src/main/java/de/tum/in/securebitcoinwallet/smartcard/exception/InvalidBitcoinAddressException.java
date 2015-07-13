package de.tum.in.securebitcoinwallet.smartcard.exception;

/**
 * Bitcoin address is not valid.
 *
 * @author Benedikt Schlagberger
 */
public class InvalidBitcoinAddressException extends SmartCardException {
  public InvalidBitcoinAddressException() {
  }

  public InvalidBitcoinAddressException(String s) {
    super(s);
  }
}

