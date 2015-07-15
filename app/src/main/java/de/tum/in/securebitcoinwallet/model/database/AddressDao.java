package de.tum.in.securebitcoinwallet.model.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.AddressMapper;
import de.tum.in.securebitcoinwallet.model.dto.AddressDto;
import de.tum.in.securebitcoinwallet.model.exception.AddressNameAlreadyInUseException;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

import static de.tum.in.securebitcoinwallet.model.Address.COL_ADDRESS;
import static de.tum.in.securebitcoinwallet.model.Address.COL_AMOUNT;
import static de.tum.in.securebitcoinwallet.model.Address.COL_NAME;
import static de.tum.in.securebitcoinwallet.model.Address.COL_PUBLIC_KEY;
import static de.tum.in.securebitcoinwallet.model.Address.COL_TOTAL_RECEIVED;
import static de.tum.in.securebitcoinwallet.model.Address.COL_TOTAL_SENT;
import static de.tum.in.securebitcoinwallet.model.Address.TABLE;

/**
 * DAO (Data Access Object) for accessing the list of addresses
 *
 * @author Hannes Dorfmann
 */
public class AddressDao extends AbsDao {

  @Override public void createTable(SQLiteDatabase sqLiteDatabase) {

    CREATE_TABLE(TABLE, COL_ADDRESS + " TEXT PRIMARY KEY NOT NULL", COL_NAME + " TEXT",
        COL_AMOUNT + " INTEGER DEFAULT 0", COL_TOTAL_RECEIVED + " INTEGER DEFAULT 0",
        COL_TOTAL_SENT + " INTEGER DEFAULT 0", COL_PUBLIC_KEY + " BLOB").execute(sqLiteDatabase);
  }

  @Override public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

  }

  /**
   * Get the list of addresses
   *
   * @return list of all addresses
   */
  public Observable<List<Address>> getAddresses() {
    return defer(query(SELECT(COL_ADDRESS, COL_NAME, COL_AMOUNT, COL_TOTAL_RECEIVED, COL_TOTAL_SENT,
        COL_PUBLIC_KEY).FROM(TABLE)).map(new Func1<SqlBrite.Query, List<Address>>() {
      @Override public List<Address> call(SqlBrite.Query query) {
        return AddressMapper.list(query.run());
      }
    }));
  }

  /**
   * Get details for a certain Address
   *
   * @param address The address
   * @param subscribeForUpdates true, if you want to be notified about changes?
   * @return Address or null if not found
   */
  public Observable<Address> getAddress(String address, boolean subscribeForUpdates) {
    return defer(query(SELECT("*").FROM(TABLE).WHERE(COL_ADDRESS + " = ?"), subscribeForUpdates,
        address).map(new Func1<SqlBrite.Query, Address>() {
      @Override public Address call(SqlBrite.Query query) {
        return AddressMapper.single(query.run());
      }
    }));
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
        .totalSent(a.getTotalSent())
        .publicKey(a.getPublicKey());

    if (a.getName() == null) {
      builder.nameAsNull();
    } else {
      builder.name(a.getName());
    }

    ContentValues cv = builder.build();

    return defer(insert(TABLE, cv, SQLiteDatabase.CONFLICT_REPLACE).map(new Func1<Long, Address>() {
      @Override public Address call(Long aLong) {
        return a;
      }
    }));
  }

  /**
   * Deletes an address
   *
   * @param address The address to delete
   * @return The number of deleted entries (Should always be 1 or 0)
   */
  public Observable<Integer> delete(final Address address) {
    return defer(delete(TABLE, COL_ADDRESS + " = ?", address.getAddress()));
  }

  /**
   * Checks if a given address name can be used or if it's already in use
   *
   * @param name the desired address name
   * @return true, or an {@link AddressNameAlreadyInUseException} if not name available
   */
  public Observable<Boolean> isAddressNameAvailable(final String name) {

    return defer(query(SELECT("*").FROM(TABLE).WHERE(COL_NAME + " = ?"), false, name)).flatMap(
        new Func1<SqlBrite.Query, Observable<Boolean>>() {
          @Override public Observable<Boolean> call(SqlBrite.Query query) {
            boolean available = AddressMapper.single(query.run()) == null;
            if (!available) {
              return Observable.error(
                  new AddressNameAlreadyInUseException(name + " is already in use"));
            }

            return Observable.just(true);
          }
        });
  }

  /**
   * Rename an address name
   *
   * @param address the address to rename
   * @param newName the new name of the address
   * @return true if renamed
   */
  public Observable<Boolean> rename(String address, String newName) {
    ContentValues cv = AddressMapper.contentValues().name(newName).build();
    return defer(update(TABLE, cv, COL_ADDRESS + " = ?", address)).map(
        new Func1<Integer, Boolean>() {
          @Override public Boolean call(Integer integer) {
            return integer > 0;
          }
        });
  }

  /**
   * Updates the balance for a given address
   *
   * @param dto The address dto
   * @return the number of updated addresses
   */
  public Observable<Integer> updateBalance(AddressDto dto) {

    ContentValues cv = AddressMapper.contentValues()
        .amount(dto.getAmount())
        .totalReceived(dto.getTotalReceived())
        .totalSent(dto.getTotalSent())
        .build();

    return update(TABLE, cv, SQLiteDatabase.CONFLICT_IGNORE, COL_ADDRESS + " = ?",
        dto.getAddress());
  }
}
