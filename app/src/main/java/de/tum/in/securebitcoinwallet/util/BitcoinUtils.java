package de.tum.in.securebitcoinwallet.util;

import java.io.File;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
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
   * Calculates the bitcoinaddress for the given byte array containing the public key and returns
   * it
   * as a String.
   *
   * @param publicKey A byte array containing the public key to calulcate the address for
   * @return The Bitcoin address as a Base58 encoded string
   */
  public static String calculateBitcoinAddress(byte[] publicKey) {
    RIPEMD160Digest ripemd160 = new RIPEMD160Digest();
    MessageDigest sha256;
    try {
      sha256 = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }

    byte[] sha256Hash = sha256.digest(publicKey);

    byte[] ripemdHash = new byte[ripemd160.getDigestSize() + 1];
    ripemd160.update(sha256Hash, 0, sha256Hash.length);
    ripemd160.doFinal(ripemdHash, 1);

    // Set version byte
    ripemdHash[0] = 0;

    sha256Hash = sha256.digest(ripemdHash);
    sha256Hash = sha256.digest(sha256Hash);

    byte[] addressBytes = new byte[ripemdHash.length + 4];

    System.arraycopy(ripemdHash, 0, addressBytes, 0, ripemdHash.length);
    System.arraycopy(sha256Hash, 0, addressBytes, (ripemdHash.length), 4);

    return Base58.encode(addressBytes);
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

  /**
   * Get the hash of a signed transactoin
   *
   * @param signedTransaction the signed transaction as byte array
   * @return the hash as String (Base58 encoded)
   */
  public static String getHashFromSignedTransaction(byte[] signedTransaction) {

    // TODO implemnt it correctly
    RIPEMD160Digest ripemd160 = new RIPEMD160Digest();
    MessageDigest sha256;
    try {
      sha256 = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }

    byte[] sha256Hash = sha256.digest(signedTransaction);

    byte[] ripemdHash = new byte[ripemd160.getDigestSize() + 1];
    ripemd160.update(sha256Hash, 0, sha256Hash.length);
    ripemd160.doFinal(ripemdHash, 1);

    // Set version byte
    ripemdHash[0] = 0;

    sha256Hash = sha256.digest(ripemdHash);
    sha256Hash = sha256.digest(sha256Hash);

    byte[] addressBytes = new byte[ripemdHash.length + 4];

    System.arraycopy(ripemdHash, 0, addressBytes, 0, ripemdHash.length);
    System.arraycopy(sha256Hash, 0, addressBytes, (ripemdHash.length), 4);

    return Base58.encode(addressBytes);
  }
}
