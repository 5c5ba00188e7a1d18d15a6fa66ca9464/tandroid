package androidx.core.os;

import android.os.Build;
import android.os.ext.SdkExtensions;
import java.util.Locale;
import org.telegram.messenger.MediaController;

/* loaded from: classes.dex */
public abstract class BuildCompat {
    public static final int AD_SERVICES_EXTENSION_INT;
    public static final int R_EXTENSION_INT;
    public static final int S_EXTENSION_INT;
    public static final int T_EXTENSION_INT;

    private static final class Extensions30Impl {
        static final int AD_SERVICES;
        static final int R;
        static final int S;
        static final int TIRAMISU;

        static {
            int extensionVersion;
            int extensionVersion2;
            int extensionVersion3;
            int extensionVersion4;
            extensionVersion = SdkExtensions.getExtensionVersion(30);
            R = extensionVersion;
            extensionVersion2 = SdkExtensions.getExtensionVersion(31);
            S = extensionVersion2;
            extensionVersion3 = SdkExtensions.getExtensionVersion(33);
            TIRAMISU = extensionVersion3;
            extensionVersion4 = SdkExtensions.getExtensionVersion(MediaController.VIDEO_BITRATE_480);
            AD_SERVICES = extensionVersion4;
        }
    }

    static {
        int i = Build.VERSION.SDK_INT;
        R_EXTENSION_INT = i >= 30 ? Extensions30Impl.R : 0;
        S_EXTENSION_INT = i >= 30 ? Extensions30Impl.S : 0;
        T_EXTENSION_INT = i >= 30 ? Extensions30Impl.TIRAMISU : 0;
        AD_SERVICES_EXTENSION_INT = i >= 30 ? Extensions30Impl.AD_SERVICES : 0;
    }

    public static boolean isAtLeastNMR1() {
        return Build.VERSION.SDK_INT >= 25;
    }

    protected static boolean isAtLeastPreReleaseCodename(String str, String str2) {
        if ("REL".equals(str2)) {
            return false;
        }
        Locale locale = Locale.ROOT;
        return str2.toUpperCase(locale).compareTo(str.toUpperCase(locale)) >= 0;
    }

    public static boolean isAtLeastR() {
        return Build.VERSION.SDK_INT >= 30;
    }

    public static boolean isAtLeastT() {
        int i = Build.VERSION.SDK_INT;
        return i >= 33 || (i >= 32 && isAtLeastPreReleaseCodename("Tiramisu", Build.VERSION.CODENAME));
    }
}
