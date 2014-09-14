package youten.redo.seikenwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class WearActivity extends Activity {

    private ToggleButton mServiceStartedToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // スクリーン常時ON
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_wear);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mServiceStartedToggleButton = (ToggleButton) stub.findViewById(R.id.service_button);
                mServiceStartedToggleButton.setChecked(Pref.isServiceStarted(getApplicationContext()));
                mServiceStartedToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) { // off -> on
                            startService(new Intent(GestureService.ACTION_START_GESTURE_SERVICE));
                        } else { // on -> off
                            startService(new Intent(GestureService.ACTION_STOP_GESTURE_SERVICE));
                        }
                    }
                });
           }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // スクリーン常時ONフラグ解除
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
