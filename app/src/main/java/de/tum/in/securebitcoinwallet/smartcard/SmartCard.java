package de.tum.in.securebitcoinwallet.smartcard;

import android.content.Context;
import com.secureflashcard.sfclibrary.SfcTerminal;
import de.tum.in.securebitcoinwallet.smartcard.exception.SmartCardException;
import de.tum.in.securebitcoinwallet.smartcard.exception.SmartCardNotConnectedException;

/**
 * Basic interface for the smartcard. Sends and receives commands to and from the card.
 *
 * @author Benedikt Schlagberger
 */
public class SmartCard {

  /**
   * APDU for selection of the Secure Bitcoin Wallet applet
   */
  private static final byte[] SELECT_APPLET_INSTRUCTION = {
      0x00, (byte) 0xA4, 0x04, 0x00, // Select AID instruction
      0x0A, // AID length
      0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x00 // AID itself
  };

  private final SfcTerminal sfcTerminal;

  /**
   * Constructor. Initializes the communication with the smartcard.
   */
  public SmartCard(Context context) {
    sfcTerminal = new SfcTerminal(context);
  }

  /**
   * Checks, if communication with the smartcard is possible.
   *
   * @return True, when the smartcard is connected, false otherwise.
   */
  public boolean isSmartCardConnected() {
    return sfcTerminal.isCardPresent();
  }

  /**
   * Sends an APDU command to the smartcard. Returns the received response.
   *
   * @param apdu The APDU command. See JavaCard specification for details.
   * @return The response of the command, or null, if no response has been generated.
   * @throws SmartCardNotConnectedException If the smartcard could not be found
   * @throws SmartCardException If the communication was not successful or the applet could not be
   * selected.
   */
  public APDUResponse sendAPDU(APDUCommand apdu) throws SmartCardException {
    try {
      sfcTerminal.connect();
    } catch (Exception e) {
      throw new SmartCardNotConnectedException(
          "Could not establish connection to smartcard: " + e.getMessage());
    }

    try {
      APDUResponse response = new APDUResponse(sfcTerminal.transmit(SELECT_APPLET_INSTRUCTION));
      if (!response.wasSuccessful()) {
        throw new SmartCardException("Could not select applet: " + response.getStatusCode());
      }
      return new APDUResponse(sfcTerminal.transmit(apdu.getBytes()));
    } catch (Exception e) {
      throw new SmartCardException("Error during command transmission: " + e.getMessage());
    }
  }

  /**
   * Closes the connection to the smartcard.
   */
  public void closeSession() {
    sfcTerminal.disconnect();
  }
}