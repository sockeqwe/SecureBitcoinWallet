package de.tum.in.securebitcoinwallet.common.presenter;

import android.support.annotation.VisibleForTesting;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.rx.scheduler.AndroidSchedulerTransformer;
import com.hannesdorfmann.mosby.mvp.rx.scheduler.SchedulerTransformer;
import de.tum.in.securebitcoinwallet.common.view.BitcoinMvpView;
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

  /**
   * Internal reference to the subscriber to be able to unsibscribe when activitiy finishes.
   */
  private Subscriber<M> subscriber;

  protected SchedulerTransformer<M> schedulerTransformer = new AndroidSchedulerTransformer<>();

  /**
   * Default construcotr
   */
  public BitcoinMvpPresenter() {
  }

  /**
   * This constructor should only be used for unit testing.
   *
   * The transformer that is used to compose observables to make them
   * run on different threads by applying {@link Observable#observeOn(Scheduler)} and {@link
   * Observable#subscribeOn(Scheduler)}. This is useful and allows us to make presenters work async
   * in
   * production, but sync during unit tests.
   *
   * @param schedulerTransformer The transformer
   */
  @VisibleForTesting BitcoinMvpPresenter(SchedulerTransformer<M> schedulerTransformer) {
    this.schedulerTransformer = schedulerTransformer;
  }

  /**
   * Creates a new anonymous subscriber and calls {@link #subscribe(Observable, Subscriber)}
   * Then the methods of presenter will be called {@link #onCompleted()}, {@link #onNext(Object)}
   * and {@link #onError(Throwable)} as the presenter itself will be the subscriber of the passed
   * observable
   *
   * @param observable The observable to subscribe.
   */
  protected void subscribe(Observable<M> observable) {
    subscribe(observable, new Subscriber<M>() {
      @Override public void onCompleted() {
        BitcoinMvpPresenter.this.onCompleted();
      }

      @Override public void onError(Throwable e) {
        BitcoinMvpPresenter.this.onError(e);
      }

      @Override public void onNext(M m) {
        BitcoinMvpPresenter.this.onNext(m);
      }
    });
  }

  /**
   * Subscribes an observable. {@link #schedulerTransformer} will be applied interanally.
   *
   * @param observable Observable to subscribe
   * @param subscriber Subscriber.
   */
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

  /**
   * Called from {@link #subscribe(Observable)} as this presenter as subscribers
   */
  protected void onCompleted() {

  }

  /**
   * Called from {@link #subscribe(Observable)} as this presenter as subscribers
   */
  protected void onError(Throwable e) {

  }

  /**
   * Called from {@link #subscribe(Observable)} as this presenter as subscribers
   */
  protected void onNext(M m) {

  }
}
