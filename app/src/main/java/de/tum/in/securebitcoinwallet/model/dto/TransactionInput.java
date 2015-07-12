package de.tum.in.securebitcoinwallet.model.dto;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Data Transfer Object representing a Transaction input
 *
 * @author Hannes Dorfmann
 */
@JsonObject public class TransactionInput {

  @JsonField String script;
  @JsonField(name = "prev_out") TransactionOutput previousOutput;

  public String getScript() {
    return script;
  }

  public TransactionOutput getPreviousOutput() {
    return previousOutput;
  }
}
