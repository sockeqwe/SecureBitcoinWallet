package de.tum.in.securebitcoinwallet.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.hannesdorfmann.sqlbrite.objectmapper.annotation.Column;
import com.hannesdorfmann.sqlbrite.objectmapper.annotation.ObjectMappable;

/**
 * @author Hannes Dorfmann
 */
@ObjectMappable @JsonObject public class Address {

  public static final String TABLE = "Address";
  public static final String COL_ADDRESS = "address";
  public static final String COL_AMOUNT = "amount";
  public static final String COL_TOTAL_SENT = "totalSent";
  public static final String COL_TOTAL_RECEIVED = "totalReceived";
  public static final String COL_NAME = "name";

  @Column(COL_ADDRESS) @JsonField(name = "address") String address;
  @Column(COL_AMOUNT) @JsonField(name = "final_balance") long amount;
  @Column(COL_TOTAL_SENT) @JsonField(name = "total_sent") long totalSent;
  @Column(COL_TOTAL_RECEIVED) @JsonField(name = "total_received") long totalReceived;
  @Column(COL_NAME) String name;

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
