package de.tum.in.securebitcoinwallet;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class AccountListActivity extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_account_list);
  }

  @Override protected Fragment getContentFragment() {
    return new PlaceHolderFragment();
  }
}
