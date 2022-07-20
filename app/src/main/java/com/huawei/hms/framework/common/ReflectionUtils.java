package com.huawei.hms.framework.common;

import android.text.TextUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
/* loaded from: classes.dex */
public class ReflectionUtils {
    private static final String TAG = "ReflectionUtils";

    private static Object invoke(Object obj, Method method, Object... objArr) throws UnsupportedOperationException {
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(obj, objArr);
        } catch (RuntimeException e) {
            Logger.e("ReflectionUtils", "RuntimeException in invoke:", e);
            return null;
        } catch (Exception e2) {
            Logger.e("ReflectionUtils", "Exception in invoke:", e2);
            return null;
        }
    }

    public static Method getMethod(Class<?> cls, String str, Class<?>... clsArr) {
        if (cls == null || str == null) {
            Logger.w("ReflectionUtils", "targetClass is  null pr name is null:");
            return null;
        }
        try {
            return cls.getDeclaredMethod(str, clsArr);
        } catch (NoSuchMethodException e) {
            Logger.e("ReflectionUtils", "NoSuchMethodException:", e);
            return null;
        } catch (SecurityException e2) {
            Logger.e("ReflectionUtils", "SecurityException:", e2);
            return null;
        }
    }

    private static void setClassType(Class<?>[] clsArr, Object obj, int i) {
        if (obj instanceof Integer) {
            clsArr[i] = Integer.TYPE;
        } else if (obj instanceof Long) {
            clsArr[i] = Long.TYPE;
        } else if (obj instanceof Double) {
            clsArr[i] = Double.TYPE;
        } else if (obj instanceof Float) {
            clsArr[i] = Float.TYPE;
        } else if (obj instanceof Boolean) {
            clsArr[i] = Boolean.TYPE;
        } else if (obj instanceof Character) {
            clsArr[i] = Character.TYPE;
        } else if (obj instanceof Byte) {
            clsArr[i] = Byte.TYPE;
        } else if (obj instanceof Void) {
            clsArr[i] = Void.TYPE;
        } else if (obj instanceof Short) {
            clsArr[i] = Short.TYPE;
        } else {
            clsArr[i] = obj.getClass();
        }
    }

    public static Object invokeStaticMethod(String str, String str2, Object... objArr) {
        Class[] clsArr;
        if (str == null) {
            return null;
        }
        if (objArr != null) {
            int length = objArr.length;
            clsArr = new Class[length];
            for (int i = 0; i < length; i++) {
                setClassType(clsArr, objArr[i], i);
            }
        } else {
            clsArr = null;
        }
        Method method = getMethod(getClass(str), str2, clsArr);
        if (method != null) {
            return invoke(null, method, objArr);
        }
        return null;
    }

    public static Object invokeStaticMethod(String str, String str2, Class<?>[] clsArr, Object... objArr) {
        Method method = getMethod(getClass(str), str2, clsArr);
        if (method == null) {
            return null;
        }
        return invoke(null, method, objArr);
    }

    public static Object getFieldObj(Object obj, String str) {
        if (obj == null || TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            Field declaredField = obj.getClass().getDeclaredField(str);
            AccessController.doPrivileged(new AnonymousClass1(declaredField));
            return declaredField.get(obj);
        } catch (IllegalAccessException e) {
            Logger.e("ReflectionUtils", "Exception in getFieldObj :: IllegalAccessException:", e);
            return null;
        } catch (IllegalArgumentException e2) {
            Logger.e("ReflectionUtils", "Exception in getFieldObj :: IllegalArgumentException:", e2);
            return null;
        } catch (NoSuchFieldException e3) {
            Logger.e("ReflectionUtils", "Exception in getFieldObj :: NoSuchFieldException:", e3);
            return null;
        } catch (SecurityException e4) {
            Logger.e("ReflectionUtils", "not security int method getFieldObj,SecurityException:", e4);
            return null;
        }
    }

    /* renamed from: com.huawei.hms.framework.common.ReflectionUtils$1 */
    /* loaded from: classes.dex */
    static class AnonymousClass1 implements PrivilegedAction {
        final /* synthetic */ Field val$field;

        AnonymousClass1(Field field) {
            this.val$field = field;
        }

        @Override // java.security.PrivilegedAction
        public Object run() {
            this.val$field.setAccessible(true);
            return null;
        }
    }

    public static Field getField(Object obj, String str) {
        if (obj != null && !TextUtils.isEmpty(str)) {
            try {
                Field declaredField = obj.getClass().getDeclaredField(str);
                AccessController.doPrivileged(new AnonymousClass2(declaredField));
                return declaredField;
            } catch (IllegalArgumentException e) {
                Logger.e("ReflectionUtils", "Exception in getField :: IllegalArgumentException:", e);
            } catch (NoSuchFieldException e2) {
                Logger.e("ReflectionUtils", "Exception in getField :: NoSuchFieldException:", e2);
            } catch (SecurityException e3) {
                Logger.e("ReflectionUtils", "not security int method getField,SecurityException:", e3);
            }
        }
        return null;
    }

    /* renamed from: com.huawei.hms.framework.common.ReflectionUtils$2 */
    /* loaded from: classes.dex */
    static class AnonymousClass2 implements PrivilegedAction {
        final /* synthetic */ Field val$field;

        AnonymousClass2(Field field) {
            this.val$field = field;
        }

        @Override // java.security.PrivilegedAction
        public Object run() {
            this.val$field.setAccessible(true);
            return null;
        }
    }

    public static Object getStaticFieldObj(String str, String str2) {
        Class<?> cls;
        if (str == null || (cls = getClass(str)) == null || TextUtils.isEmpty(str2)) {
            return null;
        }
        try {
            Field declaredField = cls.getDeclaredField(str2);
            AccessController.doPrivileged(new AnonymousClass3(declaredField));
            return declaredField.get(cls);
        } catch (IllegalAccessException e) {
            Logger.e("ReflectionUtils", "Exception in getFieldObj :: IllegalAccessException:", e);
            return null;
        } catch (IllegalArgumentException e2) {
            Logger.e("ReflectionUtils", "Exception in getFieldObj :: IllegalArgumentException:", e2);
            return null;
        } catch (NoSuchFieldException e3) {
            Logger.e("ReflectionUtils", "Exception in getFieldObj :: NoSuchFieldException:", e3);
            return null;
        } catch (SecurityException e4) {
            Logger.e("ReflectionUtils", "not security int method getStaticFieldObj,SecurityException:", e4);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.huawei.hms.framework.common.ReflectionUtils$3 */
    /* loaded from: classes.dex */
    public static class AnonymousClass3 implements PrivilegedAction {
        final /* synthetic */ Field val$field;

        AnonymousClass3(Field field) {
            this.val$field = field;
        }

        @Override // java.security.PrivilegedAction
        public Object run() {
            this.val$field.setAccessible(true);
            return null;
        }
    }

    public static boolean checkCompatible(String str) {
        try {
            tryLoadClass(str);
            return true;
        } catch (Exception unused) {
            Logger.w("ReflectionUtils", str + "ClassNotFoundException");
            return false;
        }
    }

    public static boolean checkCompatible(String str, String str2, Class<?>... clsArr) {
        try {
            if (str == null || str2 == null) {
                Logger.w("ReflectionUtils", "targetClass is  null or name is null:");
                return false;
            }
            Class.forName(str).getDeclaredMethod(str2, clsArr);
            Logger.v("ReflectionUtils", "has method : " + str2);
            return true;
        } catch (RuntimeException unused) {
            Logger.w("ReflectionUtils", str + " RuntimeException");
            return false;
        } catch (Exception unused2) {
            Logger.w("ReflectionUtils", str2 + " NoSuchMethodException");
            return false;
        }
    }

    private static void tryLoadClass(String str) throws ClassNotFoundException {
        ClassLoader classLoader = ReflectionUtils.class.getClassLoader();
        if (classLoader == null) {
            throw new ClassNotFoundException("not found classloader");
        }
        classLoader.loadClass(str);
    }

    private static Class<?> getClass(String str) {
        if (str == null) {
            return null;
        }
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException unused) {
            return null;
        }
    }
}
