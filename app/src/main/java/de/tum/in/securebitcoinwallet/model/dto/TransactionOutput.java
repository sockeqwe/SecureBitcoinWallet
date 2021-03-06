package de.tum.in.securebitcoinwallet.model.dto;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Data Transfer Object representing a Transaction output
 *
 * @author Hannes Dorfmann
 */
@JsonObject public class TransactionOutput {

  @JsonField long value;
  @JsonField String hash;
  @JsonField String script;
  @JsonField(name = "tx_index") String txIndex;
  @JsonField String n; // TODO what is that?

  public long getValue() {
    return value;
  }

  public String getHash() {
    return hash;
  }

  public String getScript() {
    return script;
  }

  public String getTxIndex() {
    return txIndex;
  }

  public String getN() {
    return n;
  }

  public void setValue(long value) {
    this.value = value;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public void setScript(String script) {
    this.script = script;
  }

  public void setTxIndex(String txIndex) {
    this.txIndex = txIndex;
  }

  public void setN(String n) {
    this.n = n;
  }
}
