package io.jamesclonk.turbojira;

import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_settings);
        //addPreferencesFromResource(R.xml.settings);
        Toast.makeText(this, "Test 123!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        return false;
    }
}
