package de.tum.in.securebitcoinwallet.smartcard;

import android.content.Context;
import com.secureflashcard.sfclibrary.SfcTerminal;
import java.io.IOException;

/**
 * Handler for the smartcard. Sends and receives commands to and from the card.
 *
 * @author Benedikt Schlagberger
 */
public class SmartCard {

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
   * Sends an APDU command to the smartcard. Returns the received response or null, if nothing has
   * been returned.
   *
   * @param apdu The APDU command. See JavaCard specification for details.
   * @return The response of the command, or null, if no response has been generated.
   * @throws IOException if the communication was unsuccessful.
   */
  public byte[] sendAPDU(byte[] apdu) throws IOException {
    // TODO implement
  }
}
