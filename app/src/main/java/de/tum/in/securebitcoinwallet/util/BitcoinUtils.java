package de.tum.in.securebitcoinwallet.util;

import java.io.File;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

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

  /**
   * Calculates the bitcoinaddress for the given ECPublicKey and returns it as a String.
   *
   * @param publicKey The public key to calulcate the address for
   * @return The Bitcoin address as a Base58 encoded string
   */
  public static String calculateBitcoinAddress(ECPublicKey publicKey) {
    return calculateBitcoinAddress(publicKey.getEncoded());
  }

  /**
   * Calculates the bitcoinaddress for the given byte array containing the public key and returns it
   * as a String.
   *
   * @param publicKey A byte array containing the public key to calulcate the address for
   * @return The Bitcoin address as a Base58 encoded string
   */
  public static String calculateBitcoinAddress(byte[] publicKey) {
    MessageDigest ripemd160;
    MessageDigest sha256;
    try {
      ripemd160 = MessageDigest.getInstance("RIPEMD-160");
      sha256 = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("RIPEMD-160 digest not found");
    }

    byte[] ripemdHash = ripemd160.digest(publicKey);
    byte[] sha256Hash = sha256.digest(ripemdHash);
    sha256Hash = sha256.digest(sha256Hash);

    byte[] addressBytes = new byte[ripemdHash.length + 5];

    // Set version byte
    addressBytes[0] = 0;
    System.arraycopy(ripemdHash, 0, addressBytes, 1, ripemdHash.length);
    System.arraycopy(sha256Hash, 0, addressBytes, (ripemdHash.length + 1), 4);

    String bitcoinAddress = Base58.encode(addressBytes);

    if (!validateBitcoinAddress(bitcoinAddress)) {
      throw new RuntimeException("Calulation of Bitcoin address failed!");
    }

    return bitcoinAddress;
  }

  /**
   * Generates the {@link ECPublicKey} for the given javacard encoded publickey byte array.
   *
   * @param data The byte array containing the encdoed public key from javacard
   * @return An instance of {@link ECPublicKey} representing the given public key
   */
  public static ECPublicKey getPublicKeyForBytes(byte[] data) {
    KeyFactory kf;
    try {
      kf = KeyFactory.getInstance("ECDSA", "BC");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Algorithm not found!");
    } catch (NoSuchProviderException e) {
      throw new RuntimeException("Provider not found!");
    }
    ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
    ECPoint point = ecSpec.getCurve().decodePoint(data);
    ECPublicKeySpec spec = new ECPublicKeySpec(point, ecSpec);
    try {
      return (ECPublicKey) kf.generatePublic(spec);
    } catch (InvalidKeySpecException e) {
      throw new RuntimeException("Key specification is invalid!");
    }
  }

  public static KeyPair getKeyPairOfFile(File keyFile) {
    // TODO implement
    throw new RuntimeException("Not Implemented!");
  }
}
