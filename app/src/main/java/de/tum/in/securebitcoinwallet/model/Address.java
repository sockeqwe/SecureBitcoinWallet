package de.tum.in.securebitcoinwallet.model;

import com.hannesdorfmann.sqlbrite.objectmapper.annotation.Column;
import com.hannesdorfmann.sqlbrite.objectmapper.annotation.ObjectMappable;

/**
 * @author Hannes Dorfmann
 */
@ObjectMappable public class Address {

  public static final String TABLE = "Address";
  public static final String COL_ADDRESS = "address";
  public static final String COL_AMOUNT = "amount";
  public static final String COL_TOTAL_SENT = "totalSent";
  public static final String COL_TOTAL_RECEIVED = "totalReceived";
  public static final String COL_NAME = "name";
  public static final String COL_PUBLIC_KEY = "publicKey";

  @Column(COL_ADDRESS) String address;
  @Column(COL_PUBLIC_KEY) byte[] publicKey;
  @Column(COL_AMOUNT) long amount;
  @Column(COL_TOTAL_SENT) long totalSent;
  @Column(COL_TOTAL_RECEIVED) long totalReceived;
  @Column(COL_NAME) String name;

  public byte[] getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(byte[] publicKey) {
    this.publicKey = publicKey;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
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

  public long getTotalSent() {
    return totalSent;
  }

  public void setTotalSent(long totalSent) {
    this.totalSent = totalSent;
  }

  public long getTotalReceived() {
    return totalReceived;
  }

  public void setTotalReceived(long totalReceived) {
    this.totalReceived = totalReceived;
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
