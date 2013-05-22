package com.cyanogenmod.settings.device;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;

public class DeviceSettings extends PreferenceActivity implements
        OnPreferenceChangeListener {

    private static final String KEYPAD_MULTIPRESS_PREF = "pref_keypad_multipress";

    private static final String KEYPAD_MULTIPRESS_PERSIST_PROP = "persist.sys.keypad_multipress_t";

    private static final String KEYPAD_MULTIPRESS_DEFAULT = "200";

    private static final String KEYPAD_MPLANG_PREF = "pref_keypad_mplang";

    private static final String KEYPAD_MPLANG_PERSIST_PROP = "persist.sys.keypad_multipress_l";

    private static final String KEYPAD_MPLANG_DEFAULT = "auto";

    private static final String REPORT_GPRS_AS_EDGE_PREF = "pref_report_gprs_as_edge";

    private static final String REPORT_GPRS_AS_EDGE_PROP = "persist.sys.report_gprs_as_edge";

    private static final String REPORT_GPRS_AS_EDGE_DEFAULT = "1";

    private static String mKeypadMultipressSum;

    private static String mKeypadMplangSum;

    private static ListPreference mKeypadMultipressPref;

    private static ListPreference mKeypadMplangPref;

    private static CheckBoxPreference mReportGprsAsEdgePref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        mKeypadMultipressSum = getString(R.string.pref_keypad_multipress_summary);
        mKeypadMplangSum = getString(R.string.pref_keypad_mplang_summary);

        mKeypadMultipressPref = (ListPreference) prefSet.findPreference(KEYPAD_MULTIPRESS_PREF);
        String keypadMultipress = SystemProperties.get(KEYPAD_MULTIPRESS_PERSIST_PROP, KEYPAD_MULTIPRESS_DEFAULT);
        mKeypadMultipressPref.setValue(keypadMultipress);
        mKeypadMultipressPref.setSummary(String.format(mKeypadMultipressSum, mKeypadMultipressPref.getEntry()));
        mKeypadMultipressPref.setOnPreferenceChangeListener(this);

        mKeypadMplangPref = (ListPreference) prefSet.findPreference(KEYPAD_MPLANG_PREF);
        String keypadMplang = SystemProperties.get(KEYPAD_MPLANG_PERSIST_PROP, KEYPAD_MPLANG_DEFAULT);
        mKeypadMplangPref.setValue(keypadMplang);
        mKeypadMplangPref.setSummary(String.format(mKeypadMplangSum, mKeypadMplangPref.getEntry()));
        mKeypadMplangPref.setOnPreferenceChangeListener(this);

        mReportGprsAsEdgePref = (CheckBoxPreference) prefSet.findPreference(REPORT_GPRS_AS_EDGE_PREF);
        String reportGprsAsEdgeVal = SystemProperties.get(REPORT_GPRS_AS_EDGE_PROP, REPORT_GPRS_AS_EDGE_DEFAULT);
        mReportGprsAsEdgePref.setChecked(reportGprsAsEdgeVal.equals("1"));
        mReportGprsAsEdgePref.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mKeypadMultipressPref.setSummary(String.format(mKeypadMultipressSum, mKeypadMultipressPref.getEntry()));
        mKeypadMplangPref.setSummary(String.format(mKeypadMplangSum, mKeypadMplangPref.getEntry()));
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mKeypadMultipressPref) {
            String keypadMultipress = (String) newValue;
            SystemProperties.set(KEYPAD_MULTIPRESS_PERSIST_PROP, keypadMultipress);
            mKeypadMultipressPref.setSummary(String.format(mKeypadMultipressSum,
                    mKeypadMultipressPref.getEntries()[mKeypadMultipressPref.findIndexOfValue(keypadMultipress)]));
            return true;
        } else if (preference == mKeypadMplangPref) {
            String keypadMplang = (String) newValue;
            SystemProperties.set(KEYPAD_MPLANG_PERSIST_PROP, keypadMplang);
            mKeypadMplangPref.setSummary(String.format(mKeypadMplangSum,
                    mKeypadMplangPref.getEntries()[mKeypadMplangPref.findIndexOfValue(keypadMplang)]));
            return true;
        } else if (preference == mReportGprsAsEdgePref) {
            Boolean checked = (Boolean) newValue;
            if (checked) {
                SystemProperties.set(REPORT_GPRS_AS_EDGE_PROP, "1");
            } else {
                SystemProperties.set(REPORT_GPRS_AS_EDGE_PROP, "0");
            }
            return true;
        }
        return false;
    }
}
