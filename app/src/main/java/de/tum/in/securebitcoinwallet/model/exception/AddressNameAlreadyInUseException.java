package de.tum.in.securebitcoinwallet.model.exception;

/**
 * Thrown to inform that the desired address name is already in use.
 *
 * @author Hannes Dorfmann
 */
public class AddressNameAlreadyInUseException extends Exception {

  public AddressNameAlreadyInUseException(String detailMessage) {
    super(detailMessage);
  }
}
