package de.tum.in.securebitcoinwallet.model;

/**
 * @author Hannes Dorfmann
 */
public class Address {

  String address;
  String publicKey;
  String name;
  long amount;

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getAmount() {
    return amount;
  }

  public void setAmount(long amount) {
    this.amount = amount;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Address address1 = (Address) o;

    return address.equals(address1.address);
  }

  @Override public int hashCode() {
    return address.hashCode();
  }
}
