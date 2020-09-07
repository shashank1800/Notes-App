package com.shashankbhat.notesapp.ui.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.shashankbhat.notesapp.R;

/**
 * Created by SHASHANK BHAT on 07-Sep-20.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

    }
}