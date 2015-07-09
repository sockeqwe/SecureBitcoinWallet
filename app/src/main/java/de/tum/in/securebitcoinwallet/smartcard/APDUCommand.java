package de.tum.in.securebitcoinwallet.smartcard;

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
    for (int i = 0; i < data.length; i++) {
      newData[i] = data[i];
    }

    for (int i = 0; i < additionalData.length; i++) {
      newData[i + data.length] = additionalData[i];
    }

    data = newData;
  }

  /**
   * Returns this APDU as a byte array.
   */
  public byte[] getBytes() {
    byte[] header = { cla, ins, p1, p2 };
    byte[] apdu = new byte[header.length + data.length];

    // assemble apdu
    for (int i = 0; i < header.length; i++) {
      apdu[i] = header[i];
    }

    apdu[header.length] = (byte) data.length;

    for (int i = 0; i < data.length; i++) {
      apdu[i + header.length + 1] = data[i];
    }

    return apdu;
  }
}
