package de.tum.in.securebitcoinwallet.common;

import android.support.annotation.VisibleForTesting;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.rx.scheduler.AndroidSchedulerTransformer;
import com.hannesdorfmann.mosby.mvp.rx.scheduler.SchedulerTransformer;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;

/**
 * Base MVP Presenter implementation to work with Rx. Assumes that only one subscription is active
 * at the same time.
 *
 * @author Hannes Dorfmann
 */
public class BitcoinMvpPresenter<V extends BitcoinMvpView, M> extends MvpBasePresenter<V> {

  private Subscriber<M> subscriber;

  /**
   * The transformer that is used to compose observables to make them
   * run on different threads by applying {@link Observable#observeOn(Scheduler)} and {@link
   * Observable#subscribeOn(Scheduler)}. This is useful and allows us to make presenters work async
   * in
   * production, but sync during unit tests.
   */
  @VisibleForTesting private SchedulerTransformer<M> schedulerTransformer =
      new AndroidSchedulerTransformer<>();

  @VisibleForTesting BitcoinMvpPresenter(SchedulerTransformer<M> schedulerTransformer) {
    this.schedulerTransformer = schedulerTransformer;
  }

  @VisibleForTesting protected void subscribe(Observable<M> observable, Subscriber<M> subscriber) {
    unsubscribe();
    this.subscriber = subscriber;
    observable.compose(schedulerTransformer).subscribe(subscriber);
  }

  /**
   * Unsubscribe any previous Rx subscriber
   */

  @VisibleForTesting protected void unsubscribe() {
    if (subscriber != null) {
      subscriber.unsubscribe();
    }
  }

  @Override public void detachView(boolean retainInstance) {
    if (!retainInstance) {
      unsubscribe();
    } else if (!getView().isChangingConfigurations()) {
      unsubscribe();
    }

    super.detachView(retainInstance);
  }
}
