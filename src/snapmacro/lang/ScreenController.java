package snapmacro.lang;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenController {

    private final Robot mCursorRobot;

    public ScreenController(Robot robot){
        mCursorRobot = robot;
    }

    public void takeScreenshot(String path) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        Rectangle fullScreen = new Rectangle(width, height);
        BufferedImage bufferedImage = mCursorRobot.createScreenCapture(fullScreen);

        path = path + File.separator + System.currentTimeMillis() + ".jpg";

        File screenshot = new File(path);
        ImageIO.write(bufferedImage, "jpg", screenshot);
    }
}
