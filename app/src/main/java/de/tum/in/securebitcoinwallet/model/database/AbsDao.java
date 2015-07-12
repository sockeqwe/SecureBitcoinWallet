package de.tum.in.securebitcoinwallet.model.database;

import com.hannesdorfmann.sqlbrite.dao.Dao;
import rx.Observable;
import rx.functions.Func0;

/**
 * Abstract base class for DAO's (Data Access Object).
 * @author Hannes Dorfmann
 */
public abstract class AbsDao extends Dao {

  /**
   * Create a defered observable
   * @param observable The observable
   * @param <T> The generic type
   * @return defered observable
   */
  protected <T> Observable<T> defer(final Observable<T> observable) {
    return Observable.defer(new Func0<Observable<T>>() {
      @Override public Observable<T> call() {
        return observable;
      }
    });
  }
}
