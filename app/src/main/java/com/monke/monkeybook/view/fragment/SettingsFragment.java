package com.monke.monkeybook.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import com.monke.monkeybook.R;

/**
 * Created by GKF on 2017/12/16.
 */

public class SettingsFragment extends PreferenceFragment {
    private static SharedPreferences preferences;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
        mContext = this.getActivity();
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pk_screen_direction)));
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = (Preference preference, Object value)-> {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);
            // Set the summary to reflect the new value.
            preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
        }
        else {
            // For all other preferences, set the summary to the value's
            preference.setSummary(stringValue);
        }
        return true;
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }


}
