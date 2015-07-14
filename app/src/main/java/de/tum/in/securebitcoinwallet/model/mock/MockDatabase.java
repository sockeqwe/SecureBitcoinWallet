package de.tum.in.securebitcoinwallet.model.mock;

import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.Transaction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Hannes Dorfmann
 */
public class MockDatabase {

  public static int addressCounter = 0;
  /**
   * A map with address as key and transation as value
   */
  private Map<Address, List<Transaction>> transactionMap = new HashMap<>();
  private Map<String, Address> addressMap = new HashMap<>();

  public MockDatabase() {

    // Generate random data

    Random random = new Random();

    int transactionSize = 20;
    long amountAddress = 20000000;

    for (int addressCounter = 0; addressCounter < 8; addressCounter++) {

      int i = addressCounter;
      Address a = new Address();
      a.setAddress("MockAddress" + i);
      a.setName("MockAddress" + i);
      a.setAmount(amountAddress);

      addressMap.put(a.getAddress(), a);

      List<Transaction> transactions = new ArrayList<>(transactionSize);
      for (int j = 0; j < transactionSize; j++) {
        Transaction t = new Transaction();
        t.setName("Rand" + i + "Addr" + j);
        t.setAddress("Rand" + i + "Addr" + j);
        t.setId(i + "-" + j);
        if (j % 2 == 0) {
          t.setAmount(amountAddress / transactionSize * 2);
        } else {
          t.setAmount(-amountAddress / transactionSize);
        }
        t.setTimestamp(System.currentTimeMillis() / 1000 - random.nextInt(60 * 24 * 60 * 60));

        transactions.add(t);
      }

      transactionMap.put(a, transactions);
    }
  }

  /**
   * Get a list of transactions associated to the given address
   */
  public List<Transaction> getTransactions(String address) {
    Address a = addressMap.get(address);
    return transactionMap.get(a);
  }

  public List<Address> getAddresses() {
    return new ArrayList<>(transactionMap.keySet());
  }

  /**
   * Adds a new Address with an empty list of transactions
   */
  public void addAddress(Address address) {
    addressMap.put(address.getAddress(), address);
    transactionMap.put(address, new ArrayList<Transaction>());
  }

  public Address getAddress(String address) {

    return addressMap.get(address);
  }

  public String nextAddressId() {
    return "MockAddress" + (++addressCounter);
  }

  public void addTransaction(String address, Transaction transaction){
    Address a = addressMap.get(address);
    List<Transaction> transactions = transactionMap.get(a);
    if (transaction == null){
      transactions = new ArrayList<>();
      transactionMap.put(a, transactions);
    }
    transactions.add(transaction);
  }
}
