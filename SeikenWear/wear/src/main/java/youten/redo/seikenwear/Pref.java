package youten.redo.seikenwear;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by youten on 2014/09/13.
 */
public class Pref {
    private static final String PREF_NAME = "wearpref";
    private static final String KEY_IS_SERVICE_STARTED = "IS_SERVICE_STARTED";

    public static final boolean isServiceStarted(Context context) {
        if (context == null) {
            return false;
        }
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_IS_SERVICE_STARTED, false);
    }

    public static final void setServiceStarted(Context context, boolean isStarted) {
        if(context == null) {
            return;
        }

        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_IS_SERVICE_STARTED, isStarted);
        editor.commit();
    }
}
