package com.microsoft.appcenter.utils;

import android.os.Bundle;
/* loaded from: classes.dex */
public abstract class InstrumentationRegistryHelper {
    private static final String[] LOCATIONS = {"androidx.test.platform.app.InstrumentationRegistry", "androidx.test.InstrumentationRegistry", "androidx.test.InstrumentationRegistry"};

    public static Bundle getArguments() {
        Exception e = null;
        for (String str : LOCATIONS) {
            try {
                return (Bundle) getClass(str).getMethod("getArguments", null).invoke(null, null);
            } catch (Exception e2) {
                e = e2;
            }
        }
        throw new IllegalStateException(e);
    }

    private static Class getClass(String str) {
        return Class.forName(str);
    }
}
