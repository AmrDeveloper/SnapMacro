package snapmacro.lang;

import java.awt.*;
import java.awt.event.InputEvent;

public class MouseController {

    private final Robot mCursorRobot;

    public MouseController(Robot robot) {
        mCursorRobot = robot;
    }

    public void mouseRightClick() {
        mCursorRobot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        mCursorRobot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    public void mouseLeftClick() {
        mCursorRobot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        mCursorRobot.delay(100);
        mCursorRobot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void mouseWheels(int value) {
        System.out.println(value);
        mCursorRobot.mouseWheel(value);
    }

    public Point getPosition() {
        return MouseInfo.getPointerInfo().getLocation();
    }
}
