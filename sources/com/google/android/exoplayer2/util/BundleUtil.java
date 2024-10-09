package com.google.android.exoplayer2.util;

import android.os.Bundle;
import android.os.IBinder;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public abstract class BundleUtil {
    private static Method getIBinderMethod;
    private static Method putIBinderMethod;

    public static IBinder getBinder(Bundle bundle, String str) {
        return Util.SDK_INT >= 18 ? bundle.getBinder(str) : getBinderByReflection(bundle, str);
    }

    private static IBinder getBinderByReflection(Bundle bundle, String str) {
        String str2;
        Method method = getIBinderMethod;
        if (method == null) {
            try {
                Method method2 = Bundle.class.getMethod("getIBinder", String.class);
                getIBinderMethod = method2;
                method2.setAccessible(true);
                method = getIBinderMethod;
            } catch (NoSuchMethodException e) {
                e = e;
                str2 = "Failed to retrieve getIBinder method";
                Log.i("BundleUtil", str2, e);
                return null;
            }
        }
        try {
            return (IBinder) method.invoke(bundle, str);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
            e = e2;
            str2 = "Failed to invoke getIBinder via reflection";
            Log.i("BundleUtil", str2, e);
            return null;
        }
    }

    public static void putBinder(Bundle bundle, String str, IBinder iBinder) {
        if (Util.SDK_INT >= 18) {
            bundle.putBinder(str, iBinder);
        } else {
            putBinderByReflection(bundle, str, iBinder);
        }
    }

    private static void putBinderByReflection(Bundle bundle, String str, IBinder iBinder) {
        Method method = putIBinderMethod;
        if (method == null) {
            try {
                Method method2 = Bundle.class.getMethod("putIBinder", String.class, IBinder.class);
                putIBinderMethod = method2;
                method2.setAccessible(true);
                method = putIBinderMethod;
            } catch (NoSuchMethodException e) {
                Log.i("BundleUtil", "Failed to retrieve putIBinder method", e);
                return;
            }
        }
        try {
            method.invoke(bundle, str, iBinder);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
            Log.i("BundleUtil", "Failed to invoke putIBinder via reflection", e2);
        }
    }
}
