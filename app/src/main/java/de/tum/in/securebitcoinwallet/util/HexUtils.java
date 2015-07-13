package de.tum.in.securebitcoinwallet.util;

/**
 * Helper class for byte and String conversions with HEX formating operations.
 *
 * @author Benedikt Schlagberger
 */
public class HexUtils {
  /**
   * Converts a String in HEX notation to the euivalent byte array.
   * @param hexString String in HEX notation
   * @return The byte array
   */
  public static byte[] hexStringToByteArray(String hexString) {
    int len = hexString.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
          + Character.digit(hexString.charAt(i+1), 16));
    }
    return data;
  }

  /**
   * Creates a String representing the given byte array in HEX notation.
   * @param data The byte array to convert
   * @return A String representing the given bytes in HEX notation
   */
  public static String getHexString(byte[] data) {
    StringBuilder builder = new StringBuilder();
    for (byte b : data) {
      builder.append(String.format("%02X", b));
    }
    return builder.toString();
  }
}
