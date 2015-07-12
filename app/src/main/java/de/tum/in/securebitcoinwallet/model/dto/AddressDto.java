package de.tum.in.securebitcoinwallet.model.dto;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * DTO (Data transfer object) for loading data from bitcoin network
 * @author Hannes Dorfmann
 */
@JsonObject public class AddressDto {

  @JsonField(name = "address") String address;
  @JsonField(name = "final_balance") long amount;
  @JsonField(name = "total_sent") long totalSent;
  @JsonField(name = "total_received") long totalReceived;
  @JsonField(name = "n_tx") int transactionsCount;

  public String getAddress() {
    return address;
  }

  public long getAmount() {
    return amount;
  }

  public long getTotalSent() {
    return totalSent;
  }

  public long getTotalReceived() {
    return totalReceived;
  }

  public int getTransactionsCount() {
    return transactionsCount;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AddressDto address1 = (AddressDto) o;

    return address.equals(address1.address);
  }

  @Override public int hashCode() {
    return address.hashCode();
  }
}
