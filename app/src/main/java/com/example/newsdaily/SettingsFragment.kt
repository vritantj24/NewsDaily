package com.example.newsdaily

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.*

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer


        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer
        setPreferencesFromResource(R.xml.pref_news,rootKey)

        val sharedPreferences = preferenceScreen.sharedPreferences
        val prefScreen: PreferenceScreen = preferenceScreen
        val count: Int = prefScreen.preferenceCount

        // Go through all of the preferences, and set up their preference summary.

        // Go through all of the preferences, and set up their preference summary.
        for (i in 0 until count) {
            val p: Preference? = prefScreen.getPreference(i)
            // You don't need to set up preference summaries for checkbox preferences because
            // they are already set up in xml using summaryOff and summary On
            if (p !is CheckBoxPreference) {
                val value = sharedPreferences.getString(p?.key, "")
                if (value != null) {
                    if (p != null) {
                        setPreferenceSummary(p, value)
                    }
                }
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        // Figure out which preference was changed

        // Figure out which preference was changed
        val preference: Preference? = findPreference(key!!)
        if (null != preference) {
            // Updates the summary for the preference
            if (preference !is CheckBoxPreference) {
                val value = sharedPreferences!!.getString(preference.key, "")
                setPreferenceSummary(preference, value!!)
            }
        }
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        TODO("Not yet implemented")
    }

    private fun setPreferenceSummary(preference: Preference, value: String) {
        if (preference is ListPreference) {
            // For list preferences, figure out the label of the selected value
            val listPreference: ListPreference = preference
            val prefIndex: Int = listPreference.findIndexOfValue(value)
            if (prefIndex >= 0) {
                // Set the summary to that label
                listPreference.summary = listPreference.entries[prefIndex]
            }
        } else if (preference is EditTextPreference) {
            // For EditTextPreferences, set the summary to the value's simple string representation.
            preference.summary = value
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceScreen.sharedPreferences
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(this)
    }
}