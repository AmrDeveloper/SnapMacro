package snapmacro.utils;

import java.util.prefs.Preferences;

public class Settings {

    private Preferences mSettingsPreference;

    private static final String SETTINGS = "settings";
    private static final String THEME = "theme";
    private static final Theme DEFAULT_THEME = Theme.WHITE;
    private OnThemeChangeListener mOnThemeChangeListener;

    public Settings() {
        mSettingsPreference = Preferences.userRoot().node(SETTINGS);
        mSettingsPreference.addPreferenceChangeListener(event -> {
            if(event.getKey().equals(THEME)){
                if(mOnThemeChangeListener == null){
                    return;
                }
                mOnThemeChangeListener.onThemeChange(Theme.valueOf(event.getNewValue()));
            }
        });
    }

    public Preferences getSettingPreference() {
        return mSettingsPreference;
    }

    public void setThemeChangeListener(OnThemeChangeListener listener) {
        mOnThemeChangeListener = listener;
    }

    public void setTheme(Theme theme) {
        mSettingsPreference.put(THEME, theme.name());
    }

    public String getTheme() {
        return mSettingsPreference.get(THEME, DEFAULT_THEME.name());
    }

    public Theme getThemeEnum(){
        String theme = mSettingsPreference.get(THEME, DEFAULT_THEME.name());
        return Theme.valueOf(theme);
    }
}
