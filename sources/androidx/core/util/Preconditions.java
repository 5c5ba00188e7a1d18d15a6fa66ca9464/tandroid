package androidx.core.util;

import android.text.TextUtils;
import java.util.Objects;
/* loaded from: classes.dex */
public final class Preconditions {
    public static <T extends CharSequence> T checkStringNotEmpty(final T string, final Object errorMessage) {
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        throw new IllegalArgumentException(String.valueOf(errorMessage));
    }

    public static <T> T checkNotNull(T reference) {
        Objects.requireNonNull(reference);
        return reference;
    }

    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference != null) {
            return reference;
        }
        throw new NullPointerException(String.valueOf(errorMessage));
    }
}
