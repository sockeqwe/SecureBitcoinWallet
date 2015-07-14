package de.tum.in.securebitcoinwallet.smartcard;

import android.content.Context;
import de.tum.in.securebitcoinwallet.smartcard.exception.AppletAlreadyInitializedException;
import de.tum.in.securebitcoinwallet.smartcard.exception.AppletNotInitializedException;
import de.tum.in.securebitcoinwallet.smartcard.exception.AuthenticationFailedExeption;
import de.tum.in.securebitcoinwallet.smartcard.exception.CardLockedException;
import de.tum.in.securebitcoinwallet.smartcard.exception.InvalidBitcoinAddressException;
import de.tum.in.securebitcoinwallet.smartcard.exception.KeyNotFoundException;
import de.tum.in.securebitcoinwallet.smartcard.exception.KeyStoreFullException;
import de.tum.in.securebitcoinwallet.smartcard.exception.SmartCardException;
import de.tum.in.securebitcoinwallet.smartcard.exception.SmartcardRuntimeException;
import de.tum.in.securebitcoinwallet.util.BitcoinUtils;
import java.security.KeyPair;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;

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
   * @throws SmartCardException If the card could not be found or is not connected.
   * @throws AppletAlreadyInitializedException If the applet has already been initialized.
   */
  public byte[] setup() throws SmartCardException {
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
   * Authenticates with the given PIN.
   *
   * @param pin The PIN to use
   * @throws CardLockedException If the card is locked and has to be unlocked with the PUK
   * @throws AuthenticationFailedExeption If the Authentication failed because a wrong PIN was used
   * @throws SmartCardException If the communication with the card failed.
   */
  public void authenticate(byte[] pin) throws SmartCardException {
    if (isPINValidated()) {
      return;
    }

    APDUCommand authenticateCommand = new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA,
        AppletInstructions.INS_AUTHENTICATE, (byte) 0, (byte) 0, pin);

    APDUResponse response = smartCard.sendAPDU(authenticateCommand);

    if (!response.wasSuccessful()) {
      switch (response.getStatusCode()) {
        case StatusCodes.CARD_LOCKED:
          throw new CardLockedException();
        case StatusCodes.AUTH_FAILED:
          throw new AuthenticationFailedExeption();
        default:
          throw new SmartcardRuntimeException("Unknown statuscode: " + response.getStatusCode());
      }
    }
  }

  /**
   * Checks if the applet has already been initialized with the setup function.
   *
   * @return True, if the applet has already been initialized, false otherwise.
   * @throws SmartCardException If the communication with the card failed.
   */
  public boolean isAppletInitialized() throws SmartCardException {
    // Calling INS_PIN_VALIDATED. No PIN is needed for this and the card reports back with
    // CONDITIONS_NOT_SATISFIED, if the setup has not yet been done.
    APDUCommand checkAuthenticationCommand =
        new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA,
            AppletInstructions.INS_PIN_VALIDATED, (byte) 0, (byte) 0);

    APDUResponse response = smartCard.sendAPDU(checkAuthenticationCommand);

    if (response.wasSuccessful()) {
      return true;
    } else {
      switch (response.getStatusCode()) {
        case StatusCodes.CONDITIONS_NOT_SATISFIED:
          return false;
        default:
          throw new SmartcardRuntimeException("Unknown statuscode: " + response.getStatusCode());
      }
    }
  }

  /**
   * Generates a new public and private keypair on the smartcard. The private key is stored on the
   * card, the public key is returned. After generation, the private key can be used to sign
   * messages by using the bitcoin address calculated from the returned public key.
   *
   * @return The public key of the generated keypair.
   * @throws SmartCardException If the communication with the card failed
   * @throws AppletNotInitializedException If the applet has not been initialized yet. This can be
   * done with {@link #setup()}.
   */
  public byte[] generateNewKey() throws SmartCardException {
    APDUCommand generateKeyCommand = new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA,
        AppletInstructions.INS_GENERATE_KEY, (byte) 0, (byte) 0);

    APDUResponse response = smartCard.sendAPDU(generateKeyCommand);

    if (!response.wasSuccessful()) {
      throw new RuntimeException(
          "Error during key generation. Unknown statuscode: " + response.getStatusCode());
    }

    // Lock the card.
    closeSession();

    return response.getData();
  }

  /**
   * Imports the given private key into the keystore on the smartcard.
   *
   * @param keyPair The keypair containing the private key to import. Has to be 256 bits
   * @throws KeyStoreFullException If no more space is left on the smartcard
   * @throws SmartCardException If communication with the smartcard failed
   */
  public void importKey(KeyPair keyPair) throws SmartCardException {
    ECPrivateKey privateKey;
    if (!(keyPair.getPrivate() instanceof ECPrivateKey)) {
      throw new RuntimeException("Not a key suitable for ECDSA/Bitcoin");
    }
    privateKey = (ECPrivateKey) keyPair.getPrivate();

    byte[] secret = privateKey.getEncoded();

    if (secret.length != 32) {
      throw new RuntimeException("Private key has wrong length!");
    }
    String bitcoinAddressString =
        BitcoinUtils.calculateBitcoinAddress((ECPublicKey) keyPair.getPublic());
    byte[] bitcoinAddress = bitcoinAddressString.getBytes();

    APDUCommand importKeyCommand = new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA,
        AppletInstructions.INS_IMPORT_PRIVATE_KEY, (byte) bitcoinAddress.length,
        (byte) secret.length, bitcoinAddress);
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
   * @throws InvalidBitcoinAddressException If the given Bitcoin address is invalid
   * @throws KeyNotFoundException If the requested key could not be found
   * @throws SmartCardException If communication with the smartcard failed
   */
  public byte[] exportEncryptedPrivateKey(String bitcoinAddress) throws SmartCardException {
    byte[] address = bitcoinAddress.getBytes();

    validateBitcoinAddress(address);

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
   * @param bitcoinAddress The Bitcoin address for which the key should be deleted
   * @throws InvalidBitcoinAddressException If the given Bitcoin address is invalid
   * @throws SmartCardException If communication with the smartcard failed
   */
  public void deleteKey(String bitcoinAddress) throws SmartCardException {
    byte[] address = bitcoinAddress.getBytes();

    validateBitcoinAddress(address);

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
   * Signs the given transaction.
   *
   * @param hash The SHA-256 hash to sign. Has to be 32 bytes.
   * @param bitcoinAddress The Bitcoin address of which the private key should be used to sign the
   * hash
   * @return The signature of the transaction
   * @throws InvalidBitcoinAddressException If the given Bitcoin address is invalid
   * @throws KeyNotFoundException If the key for the Bitcoin address could not be found.
   * @throws SmartCardException If communication with the smartcard failed
   */
  public byte[] signSHA256Hash(byte[] hash, String bitcoinAddress) throws SmartCardException {
    // SHA256 has 256 bits.
    if (hash.length != 32) {
      throw new RuntimeException("Incorrect hash length");
    }

    selectPrivateKey(bitcoinAddress.getBytes());

    APDUCommand signCommand = new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA,
        AppletInstructions.INS_SIGN_SHA256_HASH, (byte) 0, (byte) 0, hash);

    APDUResponse response = smartCard.sendAPDU(signCommand);

    if (!response.wasSuccessful()) {
      switch (response.getStatusCode()) {
        case StatusCodes.NO_KEY_SELECTED:
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
   * Changes the PIN to the desired value.
   *
   * @param newPin The new PIN
   * @throws SmartCardException If communication with the smartcard failed
   */
  public void changePIN(byte[] newPin) throws SmartCardException {
    if (newPin.length > 8 || newPin.length < 4) {
      throw new RuntimeException("PIN has wrong length. May only be between 4 and 8 characters");
    }

    APDUCommand changePINCommand = new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA,
        AppletInstructions.INS_CHANGE_PIN, (byte) 0, (byte) 0, newPin);

    APDUResponse response = smartCard.sendAPDU(changePINCommand);

    if (!response.wasSuccessful()) {
      throw new SmartcardRuntimeException(
          "Error during changing PIN. Unknown statuscode: " + response.getStatusCode());
    }
  }

  /**
   * Unlocks the card if the PIN has been entered wrong too many times.
   *
   * @param puk The PUK from the setup feature
   * @param newPin The new PIN
   * @throws AuthenticationFailedExeption If the PUk is wrong
   * @throws SmartCardException If communication with the smartcard failed
   */
  public void unlock(byte[] puk, byte[] newPin) throws SmartCardException {
    if (puk.length != 8) {
      throw new RuntimeException("PUK has wrong length! Has to be 8 characters");
    }

    if (newPin.length < 4 || newPin.length > 8) {
      throw new RuntimeException("New PIN has wrong length! Has to be between 4 and 8 characters");
    }

    APDUCommand unlockCommand =
        new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA, AppletInstructions.INS_UNLOCK,
            (byte) puk.length, (byte) newPin.length, puk);

    unlockCommand.appendData(newPin);

    APDUResponse response = smartCard.sendAPDU(unlockCommand);

    if (!response.wasSuccessful()) {
      switch (response.getStatusCode()) {
        case StatusCodes.AUTH_FAILED:
          throw new AuthenticationFailedExeption();
        default:
          throw new SmartcardRuntimeException(
              "Error during unlock. Unknown statuscode: " + response.getStatusCode());
      }
    }
  }

  /**
   * Gets the remaining free slots for private keys.
   *
   * @throws SmartCardException If communication with the smartcard failed.
   */
  public int getFreeSlots() throws SmartCardException {
    APDUCommand getFreeSlotsCommand = new APDUCommand(AppletInstructions.SECURE_BITCOIN_WALLET_CLA,
        AppletInstructions.INS_GET_REMAINING_MEMORY, (byte) 0, (byte) 0);

    APDUResponse response = smartCard.sendAPDU(getFreeSlotsCommand);

    if (!response.wasSuccessful()) {
      throw new SmartcardRuntimeException(
          "Error during requesting free slots. Unknown statuscode: " + response.getStatusCode());
    }

    // Lock the card.
    closeSession();
    byte[] data = response.getData();

    return (data[0] << 8) + data[1];
  }

  /**
   * Validates the given Bitcoin address.
   *
   * @param address The Bitcoin address to validate.
   */
  private void validateBitcoinAddress(byte[] address) throws InvalidBitcoinAddressException {
    if (!BitcoinUtils.validateBitcoinAddress(new String(address))) {
      throw new InvalidBitcoinAddressException("Address is invalid: " + new String(address));
    }
  }

  /**
   * Selects the private key of the given address for the signing process.
   *
   * @param bitcoinAddress The bitcoin address for which the private key will be selected.
   * @throws KeyNotFoundException If the key for the given address could not be found.
   * @throws SmartCardException If communication with the card failed.
   */
  private void selectPrivateKey(byte[] bitcoinAddress) throws SmartCardException {
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
        case StatusCodes.PIN_VERIFICATION_REQUIRED:
          throw new SmartcardRuntimeException("PIN not validated. Should not have happened");
        default:
          throw new SmartcardRuntimeException("Unknown statuscode: " + response.getStatusCode());
      }
    }
  }

  /**
   * Checks whether the user is authenticated at the card.
   *
   * @return True, if the user is authenticated and may perform secure operations.
   * @throws AppletNotInitializedException If the applet has not been initialized yet
   * @throws SmartCardException If the communication with the card failed.
   */
  private boolean isPINValidated() throws SmartCardException {
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
}
