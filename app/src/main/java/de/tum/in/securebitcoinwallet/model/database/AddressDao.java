package de.tum.in.securebitcoinwallet.model.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.hannesdorfmann.sqlbrite.dao.Dao;
import com.squareup.sqlbrite.SqlBrite;
import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.AddressMapper;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

import static de.tum.in.securebitcoinwallet.model.Address.*;

/**
 * DAO (Data Access Object) for accessing the list of addresses
 *
 * @author Hannes Dorfmann
 */
public class AddressDao extends Dao {

  @Override public void createTable(SQLiteDatabase sqLiteDatabase) {

    CREATE_TABLE(TABLE, COL_ADDRESS + " TEXT PRIMARY KEY NOT NULL", COL_NAME + " TEXT",
        COL_AMOUNT + " INTEGER DEFAULT 0", COL_TOTAL_RECEIVED + " INTEGER DEFAULT 0",
        COL_TOTAL_SENT + " INTEGER DEFAULT 0").execute(sqLiteDatabase);
  }

  @Override public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

  }

  /**
   * Get the list of addresses
   * @return list of all addresses
   */
  public Observable<List<Address>> getAddresses() {
    return query(SELECT(COL_ADDRESS, COL_NAME, COL_AMOUNT, COL_TOTAL_RECEIVED, COL_TOTAL_SENT).FROM(
        TABLE)).map(new Func1<SqlBrite.Query, List<Address>>() {
      @Override public List<Address> call(SqlBrite.Query query) {
        return AddressMapper.list(query.run());
      }
    });
  }

  /**
   * Inserts a new Address or updates an already existing address (if address already exists)
   *
   * @param a The address to save
   * @return Observable with the address (same instance as parameter)
   */
  public Observable<Address> insertOrUpdateAddress(final Address a) {

    AddressMapper.ContentValuesBuilder builder = AddressMapper.contentValues()
        .address(a.getAddress())
        .amount(a.getAmount())
        .totalReceived(a.getTotalReceived())
        .totalSent(a.getTotalSent());

    if (a.getName() == null) {
      builder.nameAsNull();
    } else {
      builder.name(a.getName());
    }

    ContentValues cv = builder.build();

    return insert(TABLE, cv, SQLiteDatabase.CONFLICT_REPLACE).map(new Func1<Long, Address>() {
      @Override public Address call(Long aLong) {
        return a;
      }
    });
  }
}
