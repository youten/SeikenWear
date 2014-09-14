package youten.redo.seikenwear;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by youten on 2014/09/13.
 */
public class SettingsActivity extends PreferenceActivity {
    private static final String KEY_IP_ADDRESS = "ip_address";
    private static final String KEY_PORT = "port";
    private static final String VALUE_DEFAULT_IP_ADDRESS = "192.168.11.6";
    private static final String VALUE_DEFAULT_PORT = "8088";
    EditTextPreference mIPAddressPref;
    EditTextPreference mPortPref;

    public static String getIpAddress(Context context) {
        if (context == null) {
            return null;
        }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(KEY_IP_ADDRESS, VALUE_DEFAULT_IP_ADDRESS);
    }

    public static String getPort(Context context) {
        if (context == null) {
            return null;
        }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(KEY_PORT, VALUE_DEFAULT_PORT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        mIPAddressPref = (EditTextPreference)findPreference(KEY_IP_ADDRESS);
        mIPAddressPref.setSummary(mIPAddressPref.getText());
        mIPAddressPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mIPAddressPref.setSummary((String)newValue);
                return true;
            }
        });

        mPortPref = (EditTextPreference)findPreference(KEY_PORT);
        mPortPref.setSummary(mPortPref.getText());
        mPortPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mPortPref.setSummary((String)newValue);
                return true;
            }
        });

    }
}
