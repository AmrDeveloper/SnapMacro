package snapmacro.lang;

import javax.swing.*;
import java.awt.*;

public class KeyboardController {

    private final Robot mCursorRobot;

    public KeyboardController(Robot robot){
        mCursorRobot = robot;
    }

    public void keyboardPressKey(int keyValue){
        mCursorRobot.keyPress(keyValue);
    }
}
