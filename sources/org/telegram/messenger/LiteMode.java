package org.telegram.messenger;

import android.content.SharedPreferences;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
/* loaded from: classes.dex */
public class LiteMode {
    public static final int FLAGS_ANIMATED_EMOJI = 28;
    public static final int FLAGS_ANIMATED_STICKERS = 3;
    public static final int FLAGS_CHAT = 480;
    public static final int FLAG_ANIMATED_EMOJI_CHAT = 16;
    public static final int FLAG_ANIMATED_EMOJI_KEYBOARD = 4;
    public static final int FLAG_ANIMATED_EMOJI_REACTIONS = 8;
    public static final int FLAG_ANIMATED_STICKERS_CHAT = 2;
    public static final int FLAG_ANIMATED_STICKERS_KEYBOARD = 1;
    public static final int FLAG_AUTOPLAY_GIFS = 2048;
    public static final int FLAG_AUTOPLAY_VIDEOS = 1024;
    public static final int FLAG_CALLS_ANIMATIONS = 512;
    public static final int FLAG_CHAT_BACKGROUND = 32;
    public static final int FLAG_CHAT_BLUR = 256;
    public static final int FLAG_CHAT_FORUM_TWOCOLUMN = 64;
    public static final int FLAG_CHAT_SPOILER = 128;
    public static final int PRESET_HIGH = 4095;
    public static final int PRESET_LOW = 0;
    public static final int PRESET_MEDIUM = 4095;
    public static final int PRESET_POWER_SAVER = 0;
    private static boolean loaded;
    private static boolean powerSaverEnabled;
    private static int value;

    public static int getValue() {
        return getValue(false);
    }

    public static int getValue(boolean z) {
        if (!loaded) {
            loadPreference();
            loaded = true;
        }
        return value;
    }

    public static boolean isEnabled(int i) {
        return (i & getValue()) > 0;
    }

    public static boolean isEnabledSetting(int i) {
        return (i & getValue(true)) > 0;
    }

    public static void toggleFlag(int i) {
        toggleFlag(i, !isEnabled(i));
    }

    public static void toggleFlag(int i, boolean z) {
        int value2;
        if (z) {
            value2 = i | getValue();
        } else {
            value2 = (i ^ (-1)) & getValue();
        }
        setAllFlags(value2);
    }

    public static void setAllFlags(int i) {
        int value2 = (getValue() ^ (-1)) & i;
        if ((value2 & 28) > 0) {
            AnimatedEmojiDrawable.updateAll();
        }
        if ((value2 & 32) > 0) {
            Theme.reloadWallpaper();
        }
        value = i;
        savePreference();
    }

    public static void loadPreference() {
        int i;
        if (SharedConfig.getDevicePerformanceClass() == 0) {
            i = 0;
        } else {
            SharedConfig.getDevicePerformanceClass();
            i = 4095;
        }
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        if (!globalMainSettings.contains("lite_mode")) {
            if (globalMainSettings.contains("loopStickers")) {
                i = globalMainSettings.getBoolean("loopStickers", true) ? i | 2 : i & (-3);
            }
            if (globalMainSettings.contains("autoplay_video")) {
                i = (globalMainSettings.getBoolean("autoplay_video", true) || globalMainSettings.getBoolean("autoplay_video_liteforce", false)) ? i | 1024 : i & (-1025);
            }
            if (globalMainSettings.contains("autoplay_gif")) {
                i = globalMainSettings.getBoolean("autoplay_gif", true) ? i | FLAG_AUTOPLAY_GIFS : i & (-2049);
            }
            if (globalMainSettings.contains("chatBlur")) {
                i = globalMainSettings.getBoolean("chatBlur", true) ? i | FLAG_CHAT_BLUR : i & (-257);
            }
        }
        value = globalMainSettings.getInt("lite_mode", i);
        powerSaverEnabled = globalMainSettings.getBoolean("lite_mode_power_saver", true);
    }

    public static void savePreference() {
        MessagesController.getGlobalMainSettings().edit().putInt("lite_mode", value).putBoolean("lite_mode_power_saver", powerSaverEnabled).apply();
    }

    public static boolean isPowerSaverEnabled() {
        return powerSaverEnabled;
    }

    public static void setPowerSaverEnabled(boolean z) {
        powerSaverEnabled = z;
        savePreference();
    }
}
