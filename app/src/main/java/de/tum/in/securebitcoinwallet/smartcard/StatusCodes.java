package de.tum.in.securebitcoinwallet.smartcard;

/**
 * Error and status codes for the javacard applet.
 *
 * @author Benedikt Schlagberger
 */
public interface StatusCodes {
  /**
   * Conditions not satisfied.
   */
  short CONDITIONS_NOT_SATISFIED = (short) 0x6985;

  /**
   * Command not allowed.
   */
  short COMMAND_NOT_ALLOWED = (short) 0x6986;

  /**
   * P1 and/or P2 incorrect.
   */
  short INCORRECT_P1P2 = (short) 0x6A00;

  /**
   * Instruction not supported.
   */
  short INS_NOT_SUPPORTED = (short) 0x6D00;

  /**
   * CLA not supported.
   */
  short CLA_NOT_SUPPORTED = (short) 0x6E00;

  /**
   * Data invalid.
   */
  short DATA_INVALID = (short) 0x6984;

  /**
   * Error code thrown if entered PIN was incorrect.
   */
  short AUTH_FAILED = (short) 0x6300;

  /**
   * Authentication required.
   */
  short PIN_VERIFICATION_REQUIRED = (short) 0x6301;

  /**
   * Error code thrown if the card is locked, because the pin has been entered
   * wrong to many times.
   */
  short CARD_LOCKED = (short) 0x6983;

  /**
   * Wrong length
   */
  short WRONG_LENGTH = (short) 0x6700;

  /**
   * Used to indicate the given bitcoin address had the wrong
   * length.
   */
  short WRONG_ADDRESS_LENGTH = (short) 0x6701;

  /**
   * Used to indicate the given private key has the wrong length.
   */
  short WRONG_PRIVATE_KEY_LENGTH = (short) 0x6702;

  /**
   * Used to indicate the key is already in use and has to be deleted first.
   */
  short KEY_IS_IN_USE = (short) 0x6283;

  /**
   * Specified key could not be found.
   */
  short KEY_NOT_FOUND = (short) 0x6A88;

  /**
   * The KeyStore is full.
   */
  short KEYSTORE_FULL = (short) 0x6A84;

  /**
   * Key already in store.
   */
  short KEY_ALREADY_IN_STORE = (short) 0x6A89;
}
