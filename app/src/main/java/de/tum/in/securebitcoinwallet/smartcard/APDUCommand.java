package de.tum.in.securebitcoinwallet.smartcard;

import de.tum.in.securebitcoinwallet.util.HexUtils;

/**
 * Representation of a APDU command. Used to communicate with the javacard applet.
 */
public class APDUCommand {
  /**
   * The Command class of this APDU
   */
  private byte cla;

  /**
   * The instruction of this APDU
   */
  private byte ins;

  /**
   * Parameter 1 of this APDU
   */
  private byte p1;

  /**
   * Parameter 2 of this APDU
   */
  private byte p2;

  /**
   * Data of this APDU
   */
  private byte[] data;

  /**
   * Creates a new case 3 APDU command.
   *
   * @param cla Command class of this APDU
   * @param ins Instruction of this APDU
   * @param p1 Parameter 1 of this APDU
   * @param p2 Parameter 2 of this APDU
   * @param data Data of this APDU. May not exceed 256 bytes
   */
  public APDUCommand(byte cla, byte ins, byte p1, byte p2, byte[] data) {
    if (data.length > 256) {
      throw new RuntimeException("Data too long. May not exceed 256 bytes");
    }

    this.cla = cla;
    this.ins = ins;
    this.p1 = p1;
    this.p2 = p2;

    this.data = data;
  }

  /**
   * Creates a new case 1 APDU command.
   *
   * @param cla Command class of this APDU
   * @param ins Instruction of this APDU
   * @param p1 Parameter 1 of this APDU
   * @param p2 Parameter 2 of this APDU
   */
  public APDUCommand(byte cla, byte ins, byte p1, byte p2) {
    this.cla = cla;
    this.ins = ins;
    this.p1 = p1;
    this.p2 = p2;
  }

  /**
   * Creates a new APDU command with the provided byte array.
   */
  public APDUCommand(byte[] apdu) {
    if (apdu.length < 4) {
      throw new RuntimeException("Wrong APDU length. Has to be at least 4 bytes.");
    }
    this.cla = apdu[0];
    this.ins = apdu[1];
    this.p1 = apdu[2];
    this.p2 = apdu[3];
  }

  /**
   * Adds the given byte array to the end of data.
   *
   * @param additionalData The additional bytes to at to the data of this APDU.
   */
  public void appendData(byte[] additionalData) {
    if ((data.length + additionalData.length) > 256) {
      throw new RuntimeException("Resulting data is too long. May not exceed 256 bytes");
    }

    byte[] newData = new byte[data.length + additionalData.length];

    // assemble newData
    System.arraycopy(data, 0, newData, 0, data.length);
    System.arraycopy(additionalData, 0, newData, data.length, additionalData.length);

    data = newData;
  }

  /**
   * Returns this APDU as a byte array.
   */
  public byte[] getBytes() {
    byte[] header = { cla, ins, p1, p2 };

    if (data == null) {
      // APDU has only header
      return header;
    }

    byte[] apdu = new byte[header.length + 1 + data.length];

    // assemble apdu
    System.arraycopy(header, 0, apdu, 0, header.length);

    apdu[header.length] = (byte) data.length;

    for (int i = 0; i < data.length; i++) {
      apdu[i + header.length + 1] = data[i];
    }

    return apdu;
  }

  @Override public String toString() {
    return "APDUCommand: " + HexUtils.getHexString(getBytes());
  }
}
