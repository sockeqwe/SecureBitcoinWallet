package de.tum.in.securebitcoinwallet.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Hannes Dorfmann
 */
public class DbOpenHelper extends SQLiteOpenHelper {

  private static final int VERSION = 1;

  public DbOpenHelper(Context context) {
    super(context, "Database.db", null, VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {

  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
