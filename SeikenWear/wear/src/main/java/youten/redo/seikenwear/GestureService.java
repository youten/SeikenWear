package youten.redo.seikenwear;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by youten on 2014/09/13.
 */
public class GestureService extends Service implements SensorEventListener, Motion.MotionListener {
    private static final String TAG = GestureService.class.getSimpleName();
    private static final String PREFIX_ACTION = GestureService.class.getPackage().getName() + ".action.";
    public static final String ACTION_START_GESTURE_SERVICE = PREFIX_ACTION + "START_GESTURE_SERVICE";
    public static final String ACTION_STOP_GESTURE_SERVICE = PREFIX_ACTION + "STOP_GESTURE_SERVICE";

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Motion mMotion;
    private GoogleApiClient mGoogleApiClient;


    public void onPositionChanged(final String fromPosition, final String toPosition) {
        Log.d(TAG, "onPositionChanged from=" + fromPosition + " to=" + toPosition);
        sendToMobile(fromPosition + "/" + toPosition);
    }

    public void onFling(String fling) {
        Log.d(TAG, "onFling fling=" + fling);
        sendToMobile(fling);
    }

    private void sendToMobile(final String message) {
        if (mGoogleApiClient.isConnected()) {
            // 接続してたらモーションイベントを通知
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NodeApi.GetConnectedNodesResult nodes =
                            Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                    for (Node node : nodes.getNodes()) {
                        Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(),
                                message, null).await();
                    }
                }
            }).start();
        } else if (mGoogleApiClient.isConnecting()) {
            // 接続中は待つ
        } else {
            // 切断状態で接続中でもなければ再接続を試みる
            mGoogleApiClient.reconnect();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mMotion = new Motion(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();

        startSensor();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        stopSensor();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mMotion.pushEvent(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleCommand(intent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleCommand(Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (ACTION_START_GESTURE_SERVICE.equals(action)) {
            Log.d(TAG, "ACTION_START_GESTURE_SERVICE");
            if(!Pref.isServiceStarted(this)) {
                startSensor();
            }
        } else if (ACTION_STOP_GESTURE_SERVICE.equals(action)) {
            Log.d(TAG, "ACTION_STOP_GESTURE_SERVICE");
            if (Pref.isServiceStarted(this)) {
                stopSensor();
            }
        } else {
            Log.d(TAG, "Unknown action=" + action);
        }
    }

    private void startSensor() {
        if (mSensorManager == null) {
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        }
        if (mSensor == null) {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        }
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_UI);
        Pref.setServiceStarted(this, true);
    }

    private void stopSensor() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
            mSensorManager = null;
        }
        Pref.setServiceStarted(this, false);
    }
}

