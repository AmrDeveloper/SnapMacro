package snapmacro.lang;

import java.awt.*;
import java.io.IOException;

public class RobotController {

    private final Robot mCursorRobot;
    private final MouseController mMouseController;
    private final KeyboardController mKeyboardController;
    private final ScreenController mScreenController;

    private static RobotController mInstance = null;

    public static RobotController getInstance() {
        if (mInstance == null) {
            try {
                mInstance = new RobotController();
            } catch (AWTException e) {
                System.out.println("can't init Robot Controller");
            }
        }
        return mInstance;
    }

    private RobotController() throws AWTException {
        mCursorRobot = new Robot();
        mMouseController = new MouseController(mCursorRobot);
        mKeyboardController = new KeyboardController(mCursorRobot);
        mScreenController = new ScreenController(mCursorRobot);
    }

    public void setCursorPosition(int x, int y) {
        mCursorRobot.mouseMove(x, y);
    }

    public void setCursorPositionPlus(int xp, int yp) {
        //Get current x and y values
        int xValue = MouseInfo.getPointerInfo().getLocation().x;
        int yValue = MouseInfo.getPointerInfo().getLocation().y;

        //Update values by offset values
        xValue = xValue + xp;
        yValue = yValue + yp;

        //Update cursor position
        setCursorPosition(xValue, yValue);
    }

    public void mouseRightClick() {
        mMouseController.mouseRightClick();
    }

    public void mouseLeftClick() {
        mMouseController.mouseLeftClick();
    }

    public void mouseWheels(int value) {
        mMouseController.mouseWheels(value);
    }

    public void keyboardPressKey(int key) {
        mKeyboardController.keyboardPressKey(key);
    }

    public void keyboardReleaseKey(int key) {
        mKeyboardController.keyboardReleaseKey(key);
    }

    public void captureScreen(String path) {
        try{
            mScreenController.takeScreenshot(path);
        }catch (IOException e){
            throw new RuntimeException("Invalid Directory path");
        }
    }

    public String getCurrentPixelColor() {
        return mScreenController.getCurrentPixelColor();
    }

    public void delay(int time){
        mCursorRobot.delay(time);
    }
}
