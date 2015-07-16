package de.tum.in.securebitcoinwallet.model.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import com.squareup.sqlbrite.SqlBrite;
import de.tum.in.securebitcoinwallet.model.Transaction;
import de.tum.in.securebitcoinwallet.model.TransactionMapper;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * This class is responsible to store and manipulate transactions in the local database
 *
 * @author Hannes Dorfmann
 */
public class TransactionDao extends AbsDao {

  @Override public void createTable(SQLiteDatabase sqLiteDatabase) {

    // Create Table
    CREATE_TABLE(Transaction.TABLE_NAME, Transaction.COL_HASH_ID + " TEXT PRIMARY KEY NOT NULL",
        Transaction.COL_ADDRESS + " TEXT NOT NULL", Transaction.COL_NAME + " TEXT",
        Transaction.COL_AMOUNT + " INTEGER NOT NULL",
        Transaction.COL_SYNC_STATE + " INTEGER NOT NULL",
        Transaction.COL_TIMESTAMP + " INTEGER NOT NULL").execute(sqLiteDatabase);

    // Create Index on Address
    sqLiteDatabase.execSQL("CREATE INDEX AddressIndex ON "
        + Transaction.TABLE_NAME
        + "("
        + Transaction.COL_ADDRESS
        + ")");
  }

  @Override public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

  }

  /**
   * Get all transactions for a given address.
   *
   * @param address Bitcoin address
   * @return List of transactions
   */
  public Observable<List<Transaction>> getTransactionForAddress(@NonNull String address) {
    return defer(query(SELECT("*").FROM(Transaction.TABLE_NAME)
        .WHERE(Transaction.COL_ADDRESS + " = ?")
        .ORDER_BY(Transaction.COL_TIMESTAMP + " DESC"), address).map(
        new Func1<SqlBrite.Query, List<Transaction>>() {
          @Override public List<Transaction> call(SqlBrite.Query query) {
            return TransactionMapper.list(query.run());
          }
        }));
  }

  /**
   * Get transaction by hashId
   *
   * @param hashId The id (hash)
   * @return queried transaction or null if no transaction has been found
   */
  public Observable<Transaction> getTransactionById(@NonNull String hashId) {
    return defer(
        query(SELECT("*").FROM(Transaction.TABLE_NAME).WHERE(Transaction.COL_HASH_ID + " = ?"),
            hashId).map(new Func1<SqlBrite.Query, Transaction>() {
          @Override public Transaction call(SqlBrite.Query query) {
            return TransactionMapper.single(query.run());
          }
        }));
  }

  /**
   * Get transaction by transaction index
   *
   * @param txIndex The tranasction index
   * @return queried transaction or null if no transaction has been found

  public Observable<Transaction> getTransactionByTxIndex(@NonNull String txIndex) {
  return defer(
  query(SELECT("*").FROM(Transaction.TABLE_NAME).WHERE(Transaction.COL_TX_INDEX + " = ?"),
  txIndex).map(new Func1<SqlBrite.Query, Transaction>() {
  @Override public Transaction call(SqlBrite.Query query) {
  return TransactionMapper.single(query.run());
  }
  }));
  }
   */
  /**
   * Insert or update an existing transaction
   *
   * @param t The transaction to insert
   * @return The inserted transaction
   */
  public Observable<Transaction> insertOrUpdate(final Transaction t) {

    ContentValues cv = TransactionMapper.contentValues()
        .address(t.getAddress())
        .amount(t.getAmount())
        .id(t.getId())
        .name(t.getName())
        .syncState(t.getSyncState())
        .timestamp(t.getTimestamp())
        .build();

    return defer(insert(Transaction.TABLE_NAME, cv, SQLiteDatabase.CONFLICT_REPLACE).map(
        new Func1<Long, Transaction>() {
          @Override public Transaction call(Long aLong) {
            return t;
          }
        }));
  }

  /**
   * update a given transaction
   *
   * @param t The transaction to update
   * @return Updated transaction
   */
  public Observable<Transaction> update(final Transaction t) {

    ContentValues cv = TransactionMapper.contentValues()
        .amount(t.getAmount())
        .address(t.getAddress())
        .name(t.getName())
        .syncState(t.getSyncState())
        .timestamp(t.getTimestamp())
        .build();

    return defer(
        update(Transaction.TABLE_NAME, cv, Transaction.COL_HASH_ID + " = ?", t.getAddress()).map(
            new Func1<Integer, Transaction>() {
              @Override public Transaction call(Integer aLong) {
                return t;
              }
            }));
  }

  /**
   * Deletes a transaction by id
   *
   * @param hashId The hash id
   * @return The number of deleted transactions
   */
  public Observable<Integer> deleteTransactionById(@NonNull String hashId) {
    return defer(delete(Transaction.TABLE_NAME, Transaction.COL_HASH_ID + " = ?", hashId));
  }

  /**
   * Deletes all transactions that belong to a certain address
   *
   * @param address the address
   * @return the number of deleted transactions
   */
  public Observable<Integer> deleteTransactionForAddress(String address) {
    return defer(delete(Transaction.TABLE_NAME, Transaction.COL_ADDRESS + " = ?", address));
  }

  /**
   * Get a list of all not submitted transaction
   *
   * @return a list of transaction that have not been submitted to bitcoin network yet
   */
  public Observable<List<Transaction>> getNotSubmittedTransactions() {
    return defer(
        query(SELECT("*").FROM(Transaction.TABLE_NAME).WHERE(Transaction.COL_SYNC_STATE + " = ?"),
            false, Integer.toString(Transaction.SYNC_NOT_SUBMITTED))).map(
        new Func1<SqlBrite.Query, List<Transaction>>() {
          @Override public List<Transaction> call(SqlBrite.Query query) {
            return TransactionMapper.list(query.run());
          }
        });
  }
}
