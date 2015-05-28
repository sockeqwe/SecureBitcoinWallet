package de.tum.in.securebitcoinwallet.lock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hannesdorfmann.mosby.dagger1.Dagger1MosbyFragment;
import de.greenrobot.event.EventBus;
import de.tum.in.securebitcoinwallet.R;
import javax.inject.Inject;

/**
 * @author Hannes Dorfmann
 */
public class LockFragment extends Dagger1MosbyFragment {

  @Inject EventBus eventBus;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v =  inflater.inflate(R.layout.fragment_lock, container, false);
    ButterKnife.inject(this, v);
    return v;
  }


  @OnClick(R.id.unlockButton)
  public void onUnlockClicked(){
    eventBus.post(new UnlockEvent());
  }
}
