package androidx.core.text;

import android.icu.util.ULocale;
import android.os.Build;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

/* loaded from: classes.dex */
public abstract class ICUCompat {
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;

    static class Api21Impl {
        static String getScript(Locale locale) {
            return locale.getScript();
        }
    }

    static class Api24Impl {
        static ULocale addLikelySubtags(Object obj) {
            return ULocale.addLikelySubtags((ULocale) obj);
        }

        static ULocale forLocale(Locale locale) {
            return ULocale.forLocale(locale);
        }

        static String getScript(Object obj) {
            return ((ULocale) obj).getScript();
        }
    }

    static {
        int i = Build.VERSION.SDK_INT;
        if (i >= 21) {
            if (i < 24) {
                try {
                    sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", Locale.class);
                    return;
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
            return;
        }
        try {
            Class<?> cls = Class.forName("libcore.icu.ICU");
            sGetScriptMethod = cls.getMethod("getScript", String.class);
            sAddLikelySubtagsMethod = cls.getMethod("addLikelySubtags", String.class);
        } catch (Exception e2) {
            sGetScriptMethod = null;
            sAddLikelySubtagsMethod = null;
            Log.w("ICUCompat", e2);
        }
    }

    private static String addLikelySubtagsBelowApi21(Locale locale) {
        String locale2 = locale.toString();
        try {
            Method method = sAddLikelySubtagsMethod;
            if (method != null) {
                return (String) method.invoke(null, locale2);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            Log.w("ICUCompat", e);
        }
        return locale2;
    }

    private static String getScriptBelowApi21(String str) {
        try {
            Method method = sGetScriptMethod;
            if (method != null) {
                return (String) method.invoke(null, str);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            Log.w("ICUCompat", e);
        }
        return null;
    }

    public static String maximizeAndGetScript(Locale locale) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 24) {
            return Api24Impl.getScript(Api24Impl.addLikelySubtags(Api24Impl.forLocale(locale)));
        }
        if (i >= 21) {
            try {
                return Api21Impl.getScript((Locale) sAddLikelySubtagsMethod.invoke(null, locale));
            } catch (IllegalAccessException | InvocationTargetException e) {
                Log.w("ICUCompat", e);
                return Api21Impl.getScript(locale);
            }
        }
        String addLikelySubtagsBelowApi21 = addLikelySubtagsBelowApi21(locale);
        if (addLikelySubtagsBelowApi21 != null) {
            return getScriptBelowApi21(addLikelySubtagsBelowApi21);
        }
        return null;
    }
}
