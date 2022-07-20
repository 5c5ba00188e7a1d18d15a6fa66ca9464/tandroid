package org.telegram.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import org.telegram.messenger.ApplicationLoader;
/* loaded from: classes.dex */
public class LauncherIconController {
    public static void tryFixLauncherIconIfNeeded() {
        for (LauncherIcon launcherIcon : LauncherIcon.values()) {
            if (isEnabled(launcherIcon)) {
                return;
            }
        }
        setIcon(LauncherIcon.DEFAULT);
    }

    public static boolean isEnabled(LauncherIcon launcherIcon) {
        Context context = ApplicationLoader.applicationContext;
        int componentEnabledSetting = context.getPackageManager().getComponentEnabledSetting(launcherIcon.getComponentName(context));
        if (componentEnabledSetting != 1) {
            return componentEnabledSetting == 0 && launcherIcon == LauncherIcon.DEFAULT;
        }
        return true;
    }

    public static void setIcon(LauncherIcon launcherIcon) {
        Context context = ApplicationLoader.applicationContext;
        PackageManager packageManager = context.getPackageManager();
        LauncherIcon[] values = LauncherIcon.values();
        int length = values.length;
        for (int i = 0; i < length; i++) {
            LauncherIcon launcherIcon2 = values[i];
            packageManager.setComponentEnabledSetting(launcherIcon2.getComponentName(context), launcherIcon2 == launcherIcon ? 1 : 2, 1);
        }
    }

    /* loaded from: classes3.dex */
    public enum LauncherIcon {
        DEFAULT("DefaultIcon", 2131165523, 2131492893, 2131624370),
        VINTAGE("VintageIcon", 2131165518, 2131492887, 2131624374),
        AQUA("AquaIcon", 2131165512, 2131492893, 2131624368),
        PREMIUM("PremiumIcon", 2131165509, 2131492872, 2131624372, true),
        TURBO("TurboIcon", 2131165515, 2131492881, 2131624373, true),
        NOX("NoxIcon", 2131165506, 2131492893, 2131624371, true);
        
        public final int background;
        private ComponentName componentName;
        public final int foreground;
        public final String key;
        public final boolean premium;
        public final int title;

        public ComponentName getComponentName(Context context) {
            if (this.componentName == null) {
                String packageName = context.getPackageName();
                this.componentName = new ComponentName(packageName, "org.telegram.messenger." + this.key);
            }
            return this.componentName;
        }

        LauncherIcon(String str, int i, int i2, int i3) {
            this(str, i, i2, i3, false);
        }

        LauncherIcon(String str, int i, int i2, int i3, boolean z) {
            this.key = str;
            this.background = i;
            this.foreground = i2;
            this.title = i3;
            this.premium = z;
        }
    }
}
