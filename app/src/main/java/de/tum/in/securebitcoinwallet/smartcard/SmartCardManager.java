package de.tum.in.securebitcoinwallet.smartcard;

import android.content.Context;

/**
 * Manager providing functions for the communication with the smartcard.
 *
 * @author Benedikt Schlagberger
 */
public class SmartCardManager {
  private final SmartCard smartCard;

  /**
   * Constructor. Initializes the Smartcard and manages its state.
   * @param context
   */
  public SmartCardManager(Context context) {
    this.smartCard = new SmartCard(context);
  }

  public byte[] signTransactionHash() {
    return new byte[0];
  }

  private byte[] setup() {
    APDUCommand
  }

}
