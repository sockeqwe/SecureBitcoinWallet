package de.tum.in.securebitcoinwallet.smartcard;

import android.content.Context;
import de.tum.in.securebitcoinwallet.smartcard.exception.AppletAlreadyInitializedException;
import de.tum.in.securebitcoinwallet.smartcard.exception.AppletNotInitializedException;
import de.tum.in.securebitcoinwallet.smartcard.exception.KeyNotFoundException;
import de.tum.in.securebitcoinwallet.smartcard.exception.KeyStoreFullException;
import de.tum.in.securebitcoinwallet.smartcard.exception.SmartcardException;
import de.tum.in.securebitcoinwallet.smartcard.exception.SmartcardRuntimeException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Manager providing functions for the communication with the smartcard.
 *
 * @author Benedikt Schlagberger
 */
public class SmartCardManager {
  private final SmartCard smartCard;

  /**
   * Constructor. Initializes the smartcard and manages its state.
   */
  public SmartCardManager(Context context) {
    this.smartCard = new SmartCard(context);
  }

  /**
   * Triggers the setup feature on the smartcard.
   *
   * @return The PUK of the smartcard.
   * @throws SmartcardException If the card could not be found or is not connected.
   * @throws AppletAlreadyInitializedException If the applet has already been initialized.
   */
  public byte[] setup() throws SmartcardException {
    APDUCommand setupInstruction =
        new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA, AppletInstructions.INS_SETUP,
            (byte) 0, (byte) 0);

    APDUResponse response = smartCard.sendAPDU(setupInstruction);

    closeSession();

    if (!response.wasSuccessful()) {
      short statusCode = response.getStatusCode();
      switch (statusCode) {
        case StatusCodes.COMMAND_NOT_ALLOWED:
          throw new AppletAlreadyInitializedException();
        default:
          throw new SmartcardRuntimeException("Unknown Statuscode: " + statusCode);
      }
    }

    closeSession();

    return response.getData();
  }

  /**
   * Generates a new public and private keypair on the smartcard. The private key is stored on the
   * card, the public key is returned. After generation, the private key can be used to sign
   * messages by using the bitcoin address calculated from the returned public key.
   *
   * @return The public key of the generated keypair.
   * @throws SmartcardException If the communication with the card failed.
   * @throws AppletNotInitializedException If the applet has not been initialized yet. This can be
   * done with {@link #setup()}.
   */
  public PublicKey generateNewKey() throws SmartcardException {
    authenticate();

    APDUCommand generateKeyCommand = new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA,
        AppletInstructions.INS_GENERATE_KEY, (byte) 0, (byte) 0);

    APDUResponse response = smartCard.sendAPDU(generateKeyCommand);

    if (!response.wasSuccessful()) {
      throw new RuntimeException(
          "Error during key generation. Unknown statuscode: " + response.getStatusCode());
    }

    PublicKey publicKey;
    try {
      publicKey =
          KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(response.getData()));
    } catch (InvalidKeySpecException e) {
      throw new RuntimeException("Could not convert key!", e);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Algorithm not found!", e);
    }

    // Lock the card.
    closeSession();

    return publicKey;
  }

  /**
   * Imports the given private key into the keystore on the smartcard.
   *
   * @param privateKey The private key to import. Has to be 256 bits.
   * @param bitcoinAddress The Bitcoin address of the private key. // TODO: calculate?
   * @throws KeyStoreFullException If no more space is left on the smartcard
   * @throws SmartcardException If communication with the smartcard failed.
   */
  public void importKey(ECPrivateKey privateKey, String bitcoinAddress) throws SmartcardException {
    byte[] secret = privateKey.getS().toByteArray();

    if (secret.length != 32) {
      throw new RuntimeException("Private key has wrong length!");
    }

    byte[] address = bitcoinAddress.getBytes();

    validateBitcoinAddress(address);

    authenticate();

    APDUCommand importKeyCommand = new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA,
        AppletInstructions.INS_IMPORT_PRIVATE_KEY, (byte) address.length, (byte) secret.length,
        address);
    importKeyCommand.appendData(secret);

    APDUResponse response = smartCard.sendAPDU(importKeyCommand);

    if (!response.wasSuccessful()) {
      switch (response.getStatusCode()) {
        case StatusCodes.KEYSTORE_FULL:
          throw new KeyStoreFullException();
        default:
          throw new SmartcardRuntimeException(
              "Error during import. Unknown statuscode: " + response.getStatusCode());
      }
    }

    // Lock the card.
    closeSession();
  }

  /**
   * Gets the encrypted private key for the given Bitcoin address from the card.
   *
   * @param bitcoinAddress The Bitcoin address for which the private key will be fetched.
   * @return The encrypted private key
   * @throws SmartcardException If communication with the smartcard failed.
   * @throws KeyNotFoundException If the requested key could not be found.
   */
  public byte[] exportEncryptedPrivateKey(String bitcoinAddress) throws SmartcardException {
    byte[] address = bitcoinAddress.getBytes();

    validateBitcoinAddress(address);

    authenticate();

    APDUCommand exportKeyCommand = new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA,
        AppletInstructions.INS_GET_PRIVATE_KEY, (byte) 0x00, (byte) 0x00, address);

    APDUResponse response = smartCard.sendAPDU(exportKeyCommand);

    if (!response.wasSuccessful()) {
      switch (response.getStatusCode()) {
        case StatusCodes.KEY_NOT_FOUND:
          throw new RuntimeException("KeySelection failed.");
        default:
          throw new SmartcardRuntimeException(
              "Error during export. Unknown statuscode: " + response.getStatusCode());
      }
    }

    // Lock the card.
    closeSession();

    return response.getData();
  }

  /**
   * Deletes the private key specified by the given Bitcoin address.
   *
   * @param bitcoinAddress The Bitcoin address for which the key should be deleted.
   * @throws SmartcardException If communication with the smartcard failed.
   */
  public void deleteKey(String bitcoinAddress) throws SmartcardException {
    byte[] address = bitcoinAddress.getBytes();

    validateBitcoinAddress(address);

    authenticate();

    APDUCommand deleteKeyCommand = new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA,
        AppletInstructions.INS_DELETE_PRIVATE_KEY, (byte) 0x00, (byte) 0x00, address);

    APDUResponse response = smartCard.sendAPDU(deleteKeyCommand);

    if (!response.wasSuccessful()) {
      throw new SmartcardRuntimeException(
              "Error during key deletion. Unknown statuscode: " + response.getStatusCode());
    }

    // Lock the card.
    closeSession();
  }

  /**
   * Validates the given Bitcoin address.
   *
   * @param address The Bitcoin address to validate.
   */
  private void validateBitcoinAddress(byte[] address) {
    if (address.length < 26 || address.length > 35) {
      throw new RuntimeException("Bitcoin address is faulty");
    }
  }

  /**
   * Signs the given transaction.
   *
   * @param hash The SHA-256 hash to sign. Has to be 32 bytes.
   * @param bitcoinAddress The Bitcoin address of which the private key should be used to sign the
   * hash
   * @return The signature of the transaction.
   * @throws KeyNotFoundException If the key for the Bitcoin address could not be found.
   * @throws SmartcardException If communication with the smartcard failed.
   */
  public byte[] signSHA256Hash(byte[] hash, String bitcoinAddress) throws SmartcardException {
    // SHA256 has 256 bits.
    if (hash.length != 32) {
      throw new RuntimeException("Incorrect hash length");
    }

    authenticate();

    selectPrivateKey(bitcoinAddress.getBytes());

    APDUCommand signCommand = new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA,
        AppletInstructions.INS_SIGN_SHA256_HASH, (byte) 0, (byte) 0, hash);

    APDUResponse response = smartCard.sendAPDU(signCommand);

    if (!response.wasSuccessful()) {
      switch (response.getStatusCode()) {
        case StatusCodes.CONDITIONS_NOT_SATISFIED:
          throw new SmartcardRuntimeException("KeySelection failed.");
        default:
          throw new SmartcardRuntimeException(
              "Error during signing. Unknown statuscode: " + response.getStatusCode());
      }
    }

    // Lock the card.
    closeSession();

    return response.getData();
  }

  /**
   * Selects the private key of the given address for the signing process.
   *
   * @param bitcoinAddress The bitcoin address for which the private key will be selected.
   * @throws KeyNotFoundException If the key for the given address could not be found.
   * @throws SmartcardException If communication with the card failed.
   */
  private void selectPrivateKey(byte[] bitcoinAddress) throws SmartcardException {
    if (bitcoinAddress.length < 20 || bitcoinAddress.length > 40) {
      throw new RuntimeException("Bitcoin address has wrong length: " + new String(bitcoinAddress));
    }

    APDUCommand selectKeyCommand = new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA,
        AppletInstructions.INS_SELECT_KEY, (byte) 0, (byte) 0, bitcoinAddress);

    APDUResponse response = smartCard.sendAPDU(selectKeyCommand);

    if (!response.wasSuccessful()) {
      switch (response.getStatusCode()) {
        case StatusCodes.KEY_NOT_FOUND:
          throw new KeyNotFoundException();
        case StatusCodes.SECURITY_STATUS_NOT_SATISFIED:
          throw new SmartcardRuntimeException("PIN not validated. Should not have happened");
        default:
          throw new SmartcardRuntimeException("Unknown statuscode: " + response.getStatusCode());
      }
    }
  }

  /**
   * Authenticates with the given PIN. Shows a dialog asking the user for the PIN.
   *
   * @throws SmartcardException If the communication with the card failed.
   */
  private void authenticate() throws SmartcardException {
    if (isPINValidated()) {
      return;
    }

    APDUCommand authenticateCommand = new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA,
        AppletInstructions.INS_AUTHENTICATE, (byte) 0, (byte) 0, getPINFromUser());

    APDUResponse response = smartCard.sendAPDU(authenticateCommand);

    if (!response.wasSuccessful()) {
      switch (response.getStatusCode()) {
        case StatusCodes.CARD_LOCKED:
          // TODO: unlock
          break;
        case StatusCodes.AUTH_FAILED:
          // TODO: retry?
          break;
        default:
          throw new SmartcardRuntimeException("Unknown statuscode: " + response.getStatusCode());
      }
    }
  }

  /**
   * Checks whether the user is authenticated at the card.
   *
   * @return True, if the user is authenticated and may perform secure operations.
   * @throws SmartcardException If the communication with the card failed.
   * @throws AppletNotInitializedException If the applet has not been initialized yet
   */
  private boolean isPINValidated() throws SmartcardException {
    APDUCommand checkAuthenticationCommand =
        new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA,
            AppletInstructions.INS_PIN_VALIDATED, (byte) 0, (byte) 0);

    APDUResponse response = smartCard.sendAPDU(checkAuthenticationCommand);

    if (!response.wasSuccessful()) {
      switch (response.getStatusCode()) {
        case StatusCodes.CONDITIONS_NOT_SATISFIED:
          throw new AppletNotInitializedException();
        default:
          throw new SmartcardRuntimeException("Unknown statuscode: " + response.getStatusCode());
      }
    }

    return response.getData()[0] != 0;
  }

  /**
   * Has to be called after operations using the pin.
   */
  private void closeSession() {
    smartCard.closeSession();
  }

  /**
   * TODO
   */
  private byte[] getPINFromUser() {
    // TODO implement
    return new byte[0];
  }
}
