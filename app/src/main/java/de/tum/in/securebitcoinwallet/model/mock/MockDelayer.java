package de.tum.in.securebitcoinwallet.model.mock;

/**
 * @author Hannes Dorfmann
 */
public class MockDelayer {

  public static int DELAY = 3000;

  public static void delay() {

    try {
      // Simulate network delay
      Thread.sleep(DELAY);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
