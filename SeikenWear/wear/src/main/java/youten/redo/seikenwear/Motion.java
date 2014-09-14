package youten.redo.seikenwear;

import android.hardware.SensorEvent;

/**
 * Created by youten on 2014/09/01.
 */
public class Motion {
    public static final String POSITION_FRONT_UP = "FRONT"; // 腕時計上面が上を向いている
    public static final String POSITION_BACK_UP = "BACK"; // 腕時計背面が上を向いている
    public static final String POSITION_RIGHT_UP = "RIGHT"; // 右側面が上を向いている
    public static final String POSITION_LEFT_UP = "LEFT"; // 左側面が上を向いている
    public static final String POSITION_BOTTOM_UP = "BOTTOM"; // 下部側面が上を向いている
    public static final String POSITION_TOP_UP = "TOP"; // 上部側面が上を向いている
    public static final String POSITION_UNKNOWN = "Unknown"; // どの状態でもない

    // Fling:FRONTからRIGHT, LEFT, BOTTOM, TOPに傾けてすぐ戻す操作
    public static final String FLING_INSIDE = "FLING IN"; // 下部側面を下に向けてすぐ戻す
    public static final String FLING_OUTSIDE = "FLING OUT"; // 上部側面を下に向けてすぐ戻す
    public static final String FLING_LEFT = "FLING LEFT"; // 左側面を下に向けてすぐ戻す
    public static final String FLING_RIGHT = "FLING RIGHT"; // 右側面を下に向けてすぐ戻す
    public static final String NO_FLING = "NO FLING"; // どのFling操作でもない

    public interface MotionListener {
        /**
         * Position変化モーションが発生
         * @param fromPosition 旧Position
         * @param toPosition 現Position
         */
        public void onPositionChanged(String fromPosition, String toPosition);

        /**
         * Flingモーションが発生
         * @param fling 前後左右Fling種別
         */
        public void onFling(String fling);
    }

    // xyzの絶対値のいずれかの絶対値がこの値を超えている
    private static final float POSITION_IN_BORDER = 7.0f;
    private float[] mNowXYZ = new float[3];
    private String mPosition = POSITION_UNKNOWN;
    private MotionListener mListener = null;

    // fling用前回RIGHT, LEFT, BOTTOM, TOPになった時刻
    private static final long FLING_TIME_MAX = 1000; // 1000ms以内の操作であればFlingと判定
    private long mLastPreFlingTime = 0;

    public Motion(MotionListener listener) {
        mNowXYZ[0] = 0.0f;
        mNowXYZ[1] = 0.0f;
        mNowXYZ[2] = 0.0f;
        mPosition = POSITION_UNKNOWN;
        mListener = listener;
    }

    /**
     * Motionリスナを取得
     * @return Motionリスナ
     */
    public MotionListener getListener() {
        return mListener;
    }

    /**
     * Motionリスナを設定
     * @param listener Motionリスナ
     */
    public void setListener(MotionListener listener) {
        mListener = listener;
    }

    /**
     * 現Positionを取得
     * @return 現Position
     */
    public String getPosition() {
        return mPosition;
    }

    /**
     * 発生したイベントに基づきMotionGesture判定を実施
     *
     * @param event センサイベント
     * @return イベント処理が正常に完了したか
     */
    public boolean pushEvent(SensorEvent event) {
        if ((event == null) || (event.values.length != 3)) {
            return false;
        }
        // ローパスフィルタ http://android.ohwada.jp/archives/334
        mNowXYZ[0] = mNowXYZ[0] * 0.5f + event.values[0] * 0.5f;
        mNowXYZ[1] = mNowXYZ[1] * 0.5f + event.values[1] * 0.5f;
        mNowXYZ[2] = mNowXYZ[2] * 0.5f + event.values[2] * 0.5f;

        // Log.d(StraightExtensionService.LOG_TAG,
        //        String.format("x=%.1f y=%.1f z=%.1f", mNowXYZ[0], mNowXYZ[1], mNowXYZ[2]));

        // 移動判定
        String newPosition = POSITION_UNKNOWN;
        if (mNowXYZ[0] > POSITION_IN_BORDER) {
            newPosition = POSITION_RIGHT_UP;
        } else if (mNowXYZ[0] < -POSITION_IN_BORDER) {
            newPosition = POSITION_LEFT_UP;
        } else if (mNowXYZ[1] > POSITION_IN_BORDER) {
            newPosition = POSITION_TOP_UP;
        } else if (mNowXYZ[1] < -POSITION_IN_BORDER) {
            newPosition = POSITION_BOTTOM_UP;
        } else if (mNowXYZ[2] > POSITION_IN_BORDER) {
            newPosition = POSITION_FRONT_UP;
        } else if (mNowXYZ[2] < -POSITION_IN_BORDER) {
            newPosition = POSITION_BACK_UP;
        }

        if (!POSITION_UNKNOWN.equals(newPosition) && !mPosition.equals(newPosition)) {
            if (mListener != null) {
                mListener.onPositionChanged(mPosition, newPosition);

                if (mLastPreFlingTime > 0) {
                    // RLTB -> FRONT && FLING_TIME_MAX ms以内
                    long now = System.currentTimeMillis();
                    if (newPosition.equals(POSITION_FRONT_UP)
                            && (now - mLastPreFlingTime < FLING_TIME_MAX)) {
                        if (mPosition.equals(POSITION_RIGHT_UP)) {
                            mListener.onFling(FLING_LEFT);
                        } else if (mPosition.equals(POSITION_LEFT_UP)) {
                            mListener.onFling(FLING_RIGHT);
                        } else if (mPosition.equals(POSITION_TOP_UP)) {
                            mListener.onFling(FLING_INSIDE);
                        } else if (mPosition.equals(POSITION_BOTTOM_UP)) {
                            mListener.onFling(FLING_OUTSIDE);
                        }
                    }
                    mLastPreFlingTime = 0;
                } else {
                    // FRONT -> RLTB
                    if (mPosition.equals(POSITION_FRONT_UP)) {
                        if (newPosition.equals(POSITION_RIGHT_UP)
                                || newPosition.equals(POSITION_LEFT_UP)
                                || newPosition.equals(POSITION_TOP_UP)
                                || newPosition.equals(POSITION_BOTTOM_UP)) {
                            mLastPreFlingTime = System.currentTimeMillis();
                        }
                    }
                }
            }
            mPosition = newPosition;
        }

        return true;
    }
}
