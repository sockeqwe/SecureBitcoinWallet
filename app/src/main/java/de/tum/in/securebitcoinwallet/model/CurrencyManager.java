package de.tum.in.securebitcoinwallet.model;

import java.text.DecimalFormat;

/**
 * Responsible to toCustomCurrency bitcoins to a real world concurrency
 *
 * @author Hannes Dorfmann
 */
public class CurrencyManager {

  DecimalFormat df = new DecimalFormat("#.00");

  /**
   * Convert Satoshi into the currently set currency
   * @param bitcoinSatoshi The number of bitcoin in satoshi
   * @return well formatted string (incl. currency symbol)
   */
  public String toCustomCurrency(long bitcoinSatoshi) {
    // TODO implement
    String concurrencySymbol = "$";
    double converted = bitcoinSatoshi * 0.0000023768;
    return String.format("%.2f %s", converted, concurrencySymbol);
  }

  /**
   * Convert an amount of Satoshi into Bitcoin
   * @param satoshi amount of Satoshis
   * @return decimal formatted String
   */
  public String satoshiToBitcoin(double satoshi) {
    return String.format("%.2f ฿",  satoshi / 100000000);
  }
}
