package de.tum.in.securebitcoinwallet.smartcard;

import java.util.Arrays;

/**
 * Representation of a APDU response. Used to identify errors during command execution.
 *
 * @author Benedikt Schlagberger
 */
public class APDUResponse {
  /**
   * The raw response data.
   */
  byte[] response;

  /**
   * Constructor.
   *
   * @param response The response data retrieved from the smartcard. May not be <code>null</code>.
   */
  public APDUResponse(byte[] response) {
    if (response == null) {
      throw new NullPointerException("Response may not be null!");
    }
    this.response = response;
  }

  /**
   * Checks whether the given response indicates a successful command execution.
   *
   * @return True if the response signals a successful command execution, false otherwise
   */
  public boolean wasSuccessful() {
    return response.length >= 2
        && response[response.length - 2] == 0x90
        && response[response.length - 1] == 0x00;
  }

  /**
   * Data received from the card.
   */
  public byte[] getData() {
    return Arrays.copyOfRange(response, 0, response.length - 2);
  }

  /**
   * Statuscode of the reponse.
   */
  public short getStatusCode() {
    short statusCode = 0x0000;

    if (response.length < 2) {
      return statusCode;
    }

    statusCode = response[response.length - 2];
    statusCode <<= 8;
    statusCode += response[response.length - 1];

    return statusCode;
  }
}
