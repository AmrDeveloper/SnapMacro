package snapmacro.utils;

import java.awt.*;

public class CursorManager {

    public static Point getCursorPosition(){
        return MouseInfo.getPointerInfo().getLocation();
    }
}
