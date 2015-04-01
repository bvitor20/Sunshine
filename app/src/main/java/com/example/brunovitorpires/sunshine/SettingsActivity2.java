package com.example.brunovitorpires.sunshine;

import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.example.brunovitorpires.sunshine.data.WheatherContract;


public class SettingsActivity2 extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activity2);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new SettingsFragment())
                    .commit();
        }
    }


    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        private boolean mBindingPreference;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);

                bindPreferenceSummary(
                    findPreference(getString(R.string.pref_location_key)));
                bindPreferenceSummary(
                    findPreference(getString(R.string.pref_metric_key)));
        }
        private void bindPreferenceSummary(Preference preference){
            mBindingPreference = true;
            preference.setOnPreferenceChangeListener(this);
            Object value = PreferenceManager.getDefaultSharedPreferences(
                    getActivity()).getString(preference.getKey(), "");
            onPreferenceChange(preference, value);
            mBindingPreference = false;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            if (!mBindingPreference) {
                if (preference.getKey().equals(getString(R.string.pref_location_key))){
                    new FetchWeatherTask(getActivity()).execute();
                } else {
                    getActivity().getContentResolver().notifyChange(
                            WheatherContract.WeatherEntry.CONTENT_URI, null);
                }
            }
            if (preference instanceof ListPreference){
                ListPreference listPreference =
                        (ListPreference)preference;
                int index = listPreference.findIndexOfValue(stringValue);
                if (index >= 0){
                    preference.setSummary(
                            listPreference.getEntries()[index]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    }
}
