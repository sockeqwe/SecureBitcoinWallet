package de.tum.in.securebitcoinwallet.model.presentation;

import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.Transaction;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class TransactionList {

  Address address;
  List<Transaction> transactions;

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
