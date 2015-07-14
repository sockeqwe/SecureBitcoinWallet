package de.tum.in.securebitcoinwallet.common.presenter;

import de.tum.in.securebitcoinwallet.common.view.BitcoinMvpLceView;
import rx.Observable;
import rx.Subscriber;

/**
 * Base presenter class for dealing wit MVP - LCE (Loading-Content-Error) views.
 *
 * @param <V> The generic type of the view (The view Interface)
 * @param <M> The model that is managed by this presenter and displayed by the associated view
 */
public class BitcoinMvpLcePresenter<V extends BitcoinMvpLceView<M>, M>
    extends BitcoinMvpPresenter<V, M> {

  @Override protected void subscribe(Observable<M> observable, Subscriber<M> subscriber) {
    if (isViewAttached()) {
      getView().showLoading(false);
      super.subscribe(observable, subscriber);
    }
  }

  @Override protected void onNext(M m) {
    if (isViewAttached()) {
      getView().setData(m);
      getView().showContent();
    }
  }

  @Override protected void onError(Throwable e) {
    if (isViewAttached()) {
      getView().showError(e, false);
    }
  }
}
