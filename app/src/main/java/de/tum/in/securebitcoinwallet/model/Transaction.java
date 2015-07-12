package de.tum.in.securebitcoinwallet.model;

import android.support.annotation.IntDef;
import com.hannesdorfmann.sqlbrite.objectmapper.annotation.Column;
import com.hannesdorfmann.sqlbrite.objectmapper.annotation.ObjectMappable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This class represents a bitcoin Transaction (receiveing or spending money)
 *
 * @author Hannes Dorfmann
 */
@ObjectMappable public class Transaction {

  public static final String TABLE_NAME = "BitcoinTransaction";
  public static final String COL_HASH_ID = "hashId";
  public static final String COL_NAME = "name";
  public static final String COL_TX_INDEX = "txIndex";
  public static final String COL_AMOUNT = "amount";
  public static final String COL_TIMESTAMP = "timest";
  public static final String COL_ADDRESS = "address";
  public static final String COL_SYNC_STATE = "syncState";

  public static final int SYNC_WAITING_FOR_COMMITMENT = 2;
  public static final int SYNC_NOT_SUBMITTED = 0;
  public static final int SYNC_OK = 1;

  /**
   * Defines the reachable states of a given transaction
   *
   * @author Hannes Dorfmann
   */
  @IntDef({ SYNC_WAITING_FOR_COMMITMENT, SYNC_NOT_SUBMITTED, SYNC_OK })
  @Retention(RetentionPolicy.SOURCE) public @interface SyncState {
  }

  @Column(COL_HASH_ID) String id;
  @Column(COL_TX_INDEX) String txIndex;
  @Column(COL_NAME) String name;
  @Column(COL_TIMESTAMP) long timestamp;
  @Column(COL_ADDRESS) String address;
  @Column(COL_AMOUNT) long amount;
  @Column(COL_SYNC_STATE) int syncState;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public long getAmount() {
    return amount;
  }

  public void setAmount(long amount) {
    this.amount = amount;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public String getTxIndex() {
    return txIndex;
  }

  public void setTxIndex(String txIndex) {
    this.txIndex = txIndex;
  }

  @SyncState
  public int getSyncState() {
    return syncState;
  }

  public void setSyncState(@SyncState int syncState) {
    this.syncState = syncState;
  }
}
