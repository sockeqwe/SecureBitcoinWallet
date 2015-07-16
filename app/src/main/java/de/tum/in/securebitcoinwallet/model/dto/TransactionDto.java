package de.tum.in.securebitcoinwallet.model.dto;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import de.tum.in.securebitcoinwallet.model.Transaction;
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

  public void setHash(String hash) {
    this.hash = hash;
  }

  public void setTxIndex(String txIndex) {
    this.txIndex = txIndex;
  }

  public void setInputs(List<TransactionInput> inputs) {
    this.inputs = inputs;
  }

  public void setOutputs(List<TransactionOutput> outputs) {
    this.outputs = outputs;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Convers this DTO into a normal Transaction.
   * Please note that Address is missing and must be set by hand
   *
   * @return Transaction
   */
  public Transaction toTransaction() {
    Transaction t = new Transaction();
    long sum = 0;
    for (TransactionOutput o : outputs) {
      sum += o.getValue();
    }
    t.setAmount(sum);
    t.setId(hash);
    t.setTimestamp(getTimestamp());

    return t;
  }
}
