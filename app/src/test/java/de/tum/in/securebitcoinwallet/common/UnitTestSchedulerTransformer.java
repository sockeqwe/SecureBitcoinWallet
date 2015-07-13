package de.tum.in.securebitcoinwallet.common;

import com.hannesdorfmann.mosby.mvp.rx.scheduler.SchedulerTransformer;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * A {@link SchedulerTransformer} that subscribes and observes on the main unit test thread (which
 * means immediately)
 *
 * @author Hannes Dorfmann
 */
public class UnitTestSchedulerTransformer<T> implements SchedulerTransformer<T> {

  @Override public Observable<T> call(Observable<T> observable) {
    return observable.subscribeOn(Schedulers.immediate()).observeOn(Schedulers.immediate());
  }
}
