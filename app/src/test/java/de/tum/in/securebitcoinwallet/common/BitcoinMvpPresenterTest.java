package de.tum.in.securebitcoinwallet.common;

import junit.framework.Assert;
import org.junit.Test;
import rx.Observable;
import rx.functions.Func0;
import rx.observers.TestSubscriber;

/**
 * Tests for {@link BitcoinMvpPresenter} basically to ensure that Rx Subscriptions are canceled as
 * expected to avoid memory leaks
 *
 * @author Hannes Dorfmann
 */
public class BitcoinMvpPresenterTest {

  /**
   * Creates a new defered Observable that calls {@link Observable#never()}, which forces any
   * subscriber to remain subscribed without getting onNext(), onComplete() or onError()
   * notifications.
   */
  private <T> Observable<T> deferdNever() {
    return Observable.defer(new Func0<Observable<T>>() {
      @Override public Observable<T> call() {
        return Observable.never();
      }
    });
  }

  /**
   * Tests if the presenter is unsubscribed successfully when the view gets destroyed because of
   * retainingInstance = false.
   */
  @Test public void unsubscribeNotRetainingViews() {

    BitcoinMvpView view = new BitcoinMvpView() {
      @Override public boolean isChangingConfigurations() {
        return false;
      }
    };

    TestSubscriber<Object> subscriber = new TestSubscriber<>();

    BitcoinMvpPresenter<BitcoinMvpView, Object> presenter =
        new BitcoinMvpPresenter<>(new UnitTestSchedulerTransformer<>());

    presenter.attachView(view);
    presenter.subscribe(deferdNever(), subscriber);
    // cause unsubscription
    presenter.detachView(false);

    subscriber.assertNoErrors();
    subscriber.assertUnsubscribed();
  }

  /**
   * Tests if unsubscribed because of non retaining instaince even if changeConfig == true
   */
  @Test public void unsubscribeNotRetainingViewsEvenIfChangingConfig() {

    BitcoinMvpView view = new BitcoinMvpView() {
      @Override public boolean isChangingConfigurations() {
        return true;
      }
    };

    TestSubscriber<Object> subscriber = new TestSubscriber<>();

    BitcoinMvpPresenter<BitcoinMvpView, Object> presenter =
        new BitcoinMvpPresenter<>(new UnitTestSchedulerTransformer<>());

    presenter.attachView(view);
    presenter.subscribe(deferdNever(), subscriber);
    // cause unsubscription
    presenter.detachView(false);

    subscriber.assertNoErrors();
    subscriber.assertUnsubscribed();
  }

  /**
   * Test if unsubscribed because of NOT changing config
   */
  @Test public void unsubscribeNotChangingConfig() {

    BitcoinMvpView view = new BitcoinMvpView() {
      @Override public boolean isChangingConfigurations() {
        return false;
      }
    };

    TestSubscriber<Object> subscriber = new TestSubscriber<>();

    BitcoinMvpPresenter<BitcoinMvpView, Object> presenter =
        new BitcoinMvpPresenter<>(new UnitTestSchedulerTransformer<>());

    presenter.attachView(view);
    presenter.subscribe(deferdNever(), subscriber);
    // Should cause to unsubscribe any subscription
    presenter.detachView(true);

    subscriber.assertNoErrors();
    subscriber.assertUnsubscribed();
  }

  /**
   * Subscription should still be alive beacause of ChangingConfig = true AND retainingInstance =
   * true
   */
  @Test public void stillSubscribedBecauseChangingConfig() {

    BitcoinMvpView view = new BitcoinMvpView() {
      @Override public boolean isChangingConfigurations() {
        return true;
      }
    };

    TestSubscriber<Object> subscriber = new TestSubscriber<>();

    BitcoinMvpPresenter<BitcoinMvpView, Object> presenter =
        new BitcoinMvpPresenter<>(new UnitTestSchedulerTransformer<>());

    presenter.attachView(view);
    presenter.subscribe(deferdNever(), subscriber);
    // Should cause to unsubscribe any subscription
    presenter.detachView(true);

    subscriber.assertNoErrors();
    Assert.assertFalse(subscriber.isUnsubscribed());
  }
}
