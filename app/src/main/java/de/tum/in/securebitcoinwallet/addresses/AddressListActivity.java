package de.tum.in.securebitcoinwallet.addresses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.common.BaseActivity;

public class AddressListActivity extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fragement_container);
  }


  @Override protected Fragment getContentFragment() {
    return new AddressListFragment();
  }

  @Override public void startActivity(Intent intent) {
    super.startActivity(intent);
    overridePendingTransition(R.anim.swipeback_stack_right_in, R.anim.swipeback_stack_to_back);
  }
}
