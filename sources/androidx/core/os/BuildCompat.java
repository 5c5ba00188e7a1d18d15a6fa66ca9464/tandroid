package androidx.core.os;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.ext.SdkExtensions;
import java.util.Locale;
import org.telegram.messenger.MediaController;
/* loaded from: classes.dex */
public class BuildCompat {
    @SuppressLint({"CompileTimeConstant"})
    public static final int AD_SERVICES_EXTENSION_INT;
    @SuppressLint({"CompileTimeConstant"})
    public static final int R_EXTENSION_INT;
    @SuppressLint({"CompileTimeConstant"})
    public static final int S_EXTENSION_INT;
    @SuppressLint({"CompileTimeConstant"})
    public static final int T_EXTENSION_INT;

    /* loaded from: classes.dex */
    private static final class Extensions30Impl {
        static final int R = SdkExtensions.getExtensionVersion(30);
        static final int S = SdkExtensions.getExtensionVersion(31);
        static final int TIRAMISU = SdkExtensions.getExtensionVersion(33);
        static final int AD_SERVICES = SdkExtensions.getExtensionVersion((int) MediaController.VIDEO_BITRATE_480);
    }

    protected static boolean isAtLeastPreReleaseCodename(String str, String str2) {
        if ("REL".equals(str2)) {
            return false;
        }
        Locale locale = Locale.ROOT;
        return str2.toUpperCase(locale).compareTo(str.toUpperCase(locale)) >= 0;
    }

    @Deprecated
    public static boolean isAtLeastNMR1() {
        return Build.VERSION.SDK_INT >= 25;
    }

    @Deprecated
    public static boolean isAtLeastR() {
        return Build.VERSION.SDK_INT >= 30;
    }

    public static boolean isAtLeastT() {
        int i = Build.VERSION.SDK_INT;
        return i >= 33 || (i >= 32 && isAtLeastPreReleaseCodename("Tiramisu", Build.VERSION.CODENAME));
    }

    static {
        int i = Build.VERSION.SDK_INT;
        R_EXTENSION_INT = i >= 30 ? Extensions30Impl.R : 0;
        S_EXTENSION_INT = i >= 30 ? Extensions30Impl.S : 0;
        T_EXTENSION_INT = i >= 30 ? Extensions30Impl.TIRAMISU : 0;
        AD_SERVICES_EXTENSION_INT = i >= 30 ? Extensions30Impl.AD_SERVICES : 0;
    }
}
