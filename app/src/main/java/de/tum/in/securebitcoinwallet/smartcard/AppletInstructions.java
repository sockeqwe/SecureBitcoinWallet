package de.tum.in.securebitcoinwallet.smartcard;

/**
 * Instruction bytes for the javacard applet.
 * 
 * @author Benedikt Schlagberger
 */
public interface AppletInstructions {
	/**
	 * CLA instruction class for Secure Bitcoin Wallet app instructions.
	 */
	byte SECURE_BITCOIN_WALLET_CLA = (byte) 0xA0;

	/**
	 * Setup Instruction.
	 */
	byte INS_SETUP = (byte) 0x02;

	/**
	 * Unlock instructions. Used to unlock the card, if the pin has been entered
	 * wrong too many times.
	 */
	byte INS_UNLOCK = (byte) 0x22;

	/**
	 * User authentication instruction.
	 */
	byte INS_AUTHENTICATE = (byte) 0x04;

	/**
	 * Set pin instruction.
	 */
	byte INS_CHANGE_PIN = (byte) 0x06;

	/**
	 * Pin validated instruction.
	 */
	byte INS_PIN_VALIDATED = (byte) 0x08;

	/**
	 * Select key instruction.
	 */
	byte INS_SELECT_KEY = (byte) 0xAA;

	/**
	 * Sign Bitcoin transaction instruction.
	 */
	byte INS_SIGN_SHA256_HASH = (byte) 0xAC;
	
	/**
	 * Generate new key pair instruction.
	 */
	byte INS_GENERATE_KEY = (byte) 0xAD;
	
	/**
	 * Put private key instruction.
	 */
	byte INS_IMPORT_PRIVATE_KEY = (byte) 0xD8;

	/**
	 * Get private key instruction.
	 */
	byte INS_GET_PRIVATE_KEY = (byte) 0xB0;

	/**
	 * Delete private key instruction.
	 */
	byte INS_DELETE_PRIVATE_KEY = (byte) 0xE4;

	/**
	 * Get remaining memory instruction.
	 */
	byte INS_GET_REMAINING_MEMORY = (byte) 0x28;
}