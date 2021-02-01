package snapmacro.lang;

import java.awt.event.KeyEvent;

enum KeyboardKey {

    // Digit keys
    DIGIT_0("DIGIT_0", KeyEvent.VK_0),
    DIGIT_1("DIGIT_1", KeyEvent.VK_1),
    DIGIT_2("DIGIT_2", KeyEvent.VK_2),
    DIGIT_3("DIGIT_3", KeyEvent.VK_3),
    DIGIT_4("DIGIT_4", KeyEvent.VK_4),
    DIGIT_5("DIGIT_5", KeyEvent.VK_5),
    DIGIT_6("DIGIT_6", KeyEvent.VK_6),
    DIGIT_7("DIGIT_7", KeyEvent.VK_7),
    DIGIT_8("DIGIT_8", KeyEvent.VK_8),
    DIGIT_9("DIGIT_9", KeyEvent.VK_9),

    // Virtual key
    ENTER("ENTER", KeyEvent.VK_ENTER),
    BACK_SPACE("BACK_SPACE", KeyEvent.VK_BACK_SPACE),
    CAPS_LOCK("CAPS_LOCK", KeyEvent.VK_CAPS_LOCK),
    TAB("TAB", KeyEvent.VK_TAB),
    SPACE("SPACE", KeyEvent.VK_SPACE),
    ESCAPE("ESCAPE", KeyEvent.VK_ESCAPE),
    ALT("ALT", KeyEvent.VK_ALT),
    SHIFT("SHIFT", KeyEvent.VK_SHIFT),
    CONTROL("CONTROL", KeyEvent.VK_CONTROL),
    PAGE_UP("PAGE_UP", KeyEvent.VK_PAGE_UP),
    PAGE_DOWN("PAGE_DOWN", KeyEvent.VK_PAGE_DOWN),
    HOME("HOME", KeyEvent.VK_HOME),
    END("END", KeyEvent.VK_END),

    // F Keys
    F1("F1", KeyEvent.VK_F1),
    F2("F2", KeyEvent.VK_F2),
    F3("F3", KeyEvent.VK_F3),
    F4("F4", KeyEvent.VK_F4),
    F5("F5", KeyEvent.VK_F5),
    F6("F6", KeyEvent.VK_F6),
    F7("F7", KeyEvent.VK_F7),
    F8("F8", KeyEvent.VK_F8),
    F9("F9", KeyEvent.VK_F9),
    F10("F10", KeyEvent.VK_F10),
    F11("F11", KeyEvent.VK_F11),

    // F13 - F24 are used on IBM 3270 keyboard
    F12("F12", KeyEvent.VK_F12),
    F13("F13", KeyEvent.VK_F13),
    F14("F14", KeyEvent.VK_F14),
    F15("F15", KeyEvent.VK_F15),
    F16("F16", KeyEvent.VK_F16),
    F17("F17", KeyEvent.VK_F17),
    F18("F18", KeyEvent.VK_F18),
    F19("F19", KeyEvent.VK_F19),
    F20("F20", KeyEvent.VK_F20),
    F21("F21", KeyEvent.VK_F21),
    F22("F22", KeyEvent.VK_F22),
    F23("F23", KeyEvent.VK_F23),
    F24("F24", KeyEvent.VK_F24);

    private final String keyName;
    private final int keyValue;

    KeyboardKey(String key, int value) {
        keyName = key;
        keyValue = value;
    }

    public String getKeyName() {
        return keyName;
    }

    public int getKeyValue() {
        return keyValue;
    }
}