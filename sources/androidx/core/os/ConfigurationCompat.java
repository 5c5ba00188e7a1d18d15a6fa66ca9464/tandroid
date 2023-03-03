package androidx.core.os;

import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;
/* loaded from: classes.dex */
public final class ConfigurationCompat {
    public static LocaleListCompat getLocales(Configuration configuration) {
        return Build.VERSION.SDK_INT >= 24 ? LocaleListCompat.wrap(Api24Impl.getLocales(configuration)) : LocaleListCompat.create(configuration.locale);
    }

    /* loaded from: classes.dex */
    static class Api24Impl {
        static LocaleList getLocales(Configuration configuration) {
            return configuration.getLocales();
        }
    }
}
