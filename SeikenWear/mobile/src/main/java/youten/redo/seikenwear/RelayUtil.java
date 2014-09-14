package youten.redo.seikenwear;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by youten on 2014/09/13.
 */
public class RelayUtil {
    private static final String TAG = RelayUtil.class.getSimpleName();
    private static final String PATH_FLING_IN = "FLING IN";
    private static final String PATH_FLING_OUT = "FLING OUT";
    private static final String PATH_FLING_LEFT = "FLING LEFT";
    private static final String PATH_FLING_RIGHT = "FLING RIGHT";

    private static HttpClient mHttpClient = new DefaultHttpClient();

    public static void relay(Context context, String path) {
        if (context == null) {
            return;
        }
        int keycode = -1;
        boolean withShift = false;

        if (PATH_FLING_IN.equals(path)) {
            keycode = 38; // UP
        } else if (PATH_FLING_OUT.equals(path)) {
            keycode = 40; // DOWN
        } else if (PATH_FLING_LEFT.equals(path)) {
            keycode = 27; // ESC
        } else if (PATH_FLING_RIGHT.equals(path)) {
            keycode = 116; // Shift+F5
            withShift = true;
        }

        if (keycode < 0) {
            return;
        }
        String ip = SettingsActivity.getIpAddress(context);
        String port = SettingsActivity.getPort(context);
        StringBuilder sb = new StringBuilder();
        sb.append("http://").append(ip).append(":").append(port);
        sb.append("/key?key=").append(keycode);
        if (withShift) {
           sb.append("&shift=1");
        }
        final String url = sb.toString();
        new Thread(new Runnable(){
            @Override
            public void run() {
                Log.d(TAG, "url=" + url);
                httpGetString(url);
            }
        }).start();
    }

    public static final String httpGetString(String url) {
        String ret = null;
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = mHttpClient.execute(httpGet);
            if( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
                ret = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
