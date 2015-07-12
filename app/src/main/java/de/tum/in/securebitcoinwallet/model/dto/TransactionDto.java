package de.tum.in.securebitcoinwallet.model.dto;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.List;

/**
 * Data Transfer Object representing a bitcoin transaction
 *
 * @author Hannes Dorfmann
 */
@JsonObject public class TransactionDto {

  @JsonField String hash;
  @JsonField(name = "tx_index") String txIndex;
  @JsonField List<TransactionInput> inputs;
  @JsonField(name = "out") List<TransactionOutput> outputs;
  @JsonField(name = "time") long timestamp;

  public String getHash() {
    return hash;
  }

  public String getTxIndex() {
    return txIndex;
  }

  public List<TransactionInput> getInputs() {
    return inputs;
  }

  public List<TransactionOutput> getOutputs() {
    return outputs;
  }

  public long getTimestamp() {
    return timestamp;
  }
}
