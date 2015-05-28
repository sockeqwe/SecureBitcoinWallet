package de.tum.in.securebitcoinwallet;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import butterknife.InjectView;
import com.hannesdorfmann.mosby.dagger1.viewstate.lce.Dagger1MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

/**
 * @author Hannes Dorfmann
 */
public abstract class RecyclerViewFragment<M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
    extends Dagger1MvpLceViewStateFragment<SwipeRefreshLayout, M, V, P> {

  @InjectView(R.id.recyclerView)
  RecyclerView recyclerView;


  



}
