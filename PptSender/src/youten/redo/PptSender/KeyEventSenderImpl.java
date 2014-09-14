
package youten.redo.PptSender;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class KeyEventSenderImpl implements KeyEventSender {

    private Robot mRobot;

    @Override
    public void send(int keycode, boolean withShift, boolean withAlt, boolean withCtrl) {
        init();
        if (mRobot == null) {
            return;
        }
        if (withShift) {
            mRobot.keyPress(KeyEvent.VK_SHIFT);
        }
        if (withAlt) {
            mRobot.keyPress(KeyEvent.VK_ALT);
        }
        if (withCtrl) {
            mRobot.keyPress(KeyEvent.VK_CONTROL);
        }
        mRobot.keyPress(keycode);
        mRobot.keyRelease(keycode);
        if (withShift) {
            mRobot.keyRelease(KeyEvent.VK_SHIFT);
        }
        if (withAlt) {
            mRobot.keyRelease(KeyEvent.VK_ALT);
        }
        if (withCtrl) {
            mRobot.keyRelease(KeyEvent.VK_CONTROL);
        }
    }

    // 初期化されていなかったら初期化。
    private void init() {
        if (mRobot == null) {
            try {
                mRobot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
                mRobot = null;
            }
        }
    }
}
