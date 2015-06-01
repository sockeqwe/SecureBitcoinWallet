package de.tum.in.securebitcoinwallet.preferences;

import android.os.Bundle;
import de.tum.in.securebitcoinwallet.R;

/**
 * @author Benedikt Schlagberger
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.preferences);
  }
}
