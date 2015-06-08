package de.tum.in.securebitcoinwallet.model.database;

import android.content.Context;
import com.squareup.sqlbrite.SqlBrite;

/**
 * @author Hannes Dorfmann
 */
public class Dao {

  private SqlBrite db;

  public Dao(Context context) {
    db = SqlBrite.create(new DbOpenHelper(context));
  }

  public void setLogging(boolean logging) {
    db.setLoggingEnabled(logging);
  }
}
