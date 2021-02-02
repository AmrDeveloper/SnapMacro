package snapmacro.lang;

import java.awt.*;

public class KeyboardController {

    private final Robot mCursorRobot;

    public KeyboardController(Robot robot) {
        mCursorRobot = robot;
    }

    public void keyboardPressKey(int keyValue) {
        mCursorRobot.keyPress(keyValue);
    }

    public void keyboardReleaseKey(int keyValue) {
        mCursorRobot.keyRelease(keyValue);
    }
}
