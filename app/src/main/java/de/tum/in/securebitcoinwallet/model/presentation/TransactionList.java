package de.tum.in.securebitcoinwallet.model.presentation;

import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.Transaction;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class TransactionList {

  private Address address;
  private List<Transaction> transactions;

  public TransactionList(Address address, List<Transaction> transactions) {
    this.address = address;
    this.transactions = transactions;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public List<Transaction> getTransactions() {
    return transactions;
  }

  public void setTransactions(List<Transaction> transactions) {
    this.transactions = transactions;
  }
}
