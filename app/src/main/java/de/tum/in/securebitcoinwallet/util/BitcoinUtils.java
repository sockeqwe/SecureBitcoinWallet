package de.tum.in.securebitcoinwallet.util;

import java.io.File;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Arrays;

/**
 * Util methods for Bitcoin.
 *
 * @author Benedikt Schlagberger
 */
public class BitcoinUtils {

  /**
   * Checks if the given String is a valid Bitcoin address.
   *
   * @param address The address to check
   * @return True, if the String is a valid Bitcoin address, false otherwise
   */
  public static boolean validateBitcoinAddress(String address) {
    if (address == null) {
      return false;
    }

    // Check the length
    if (address.length() < 26 || address.length() > 35) {
      return false;
    }

    byte[] addressBytes = Base58.decode(address);

    // Check the version byte
    if (addressBytes[0] != 0) {
      return false;
    }

    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("BitcoinUtils.validateBitcoinAddress: SHA-256 digest not found");
    }

    md.update(addressBytes, 0, 21);
    byte[] sha256Hash = md.digest();
    sha256Hash = md.digest(sha256Hash);

    byte[] addressChecksum = Arrays.copyOfRange(addressBytes, 21, addressBytes.length);
    byte[] calculatedChecksum = Arrays.copyOfRange(sha256Hash, 0, 4);
    return Arrays.equals(addressChecksum, calculatedChecksum);
  }

  public static byte[] calculateBitcoinAddress(ECPublicKey publicKey) {
    MessageDigest ripemd160;
    MessageDigest sha256;
    try {
      ripemd160 = MessageDigest.getInstance("RIPEMD-160");
      sha256 = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(
          "RIPEMD-160 digest not found");
    }

    byte[] ripemdHash = ripemd160.digest(publicKey.getEncoded());

    // TODO implement
    throw new RuntimeException("Not Implemented!");
  }

  public static ECPublicKey getPublicKeyForBytes(byte[] data) {
    // TODO implement
    throw new RuntimeException("Not Implemented!");
  }

  public static KeyPair getKeyPairOfFile(File keyFile) {
    // TODO implement
    throw new RuntimeException("Not Implemented!");
  }
}
