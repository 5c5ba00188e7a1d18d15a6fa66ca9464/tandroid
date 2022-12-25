package com.google.gson.internal;

import com.google.gson.ReflectionAccessFilter;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;
/* loaded from: classes.dex */
public class ReflectionAccessFilterHelper {
    public static ReflectionAccessFilter.FilterResult getFilterResult(List<ReflectionAccessFilter> list, Class<?> cls) {
        for (ReflectionAccessFilter reflectionAccessFilter : list) {
            ReflectionAccessFilter.FilterResult check = reflectionAccessFilter.check(cls);
            if (check != ReflectionAccessFilter.FilterResult.INDECISIVE) {
                return check;
            }
        }
        return ReflectionAccessFilter.FilterResult.ALLOW;
    }

    public static boolean canAccess(AccessibleObject accessibleObject, Object obj) {
        return AccessChecker.INSTANCE.canAccess(accessibleObject, obj);
    }

    /* loaded from: classes.dex */
    private static abstract class AccessChecker {
        public static final AccessChecker INSTANCE;

        public abstract boolean canAccess(AccessibleObject accessibleObject, Object obj);

        private AccessChecker() {
        }

        /* JADX WARN: Removed duplicated region for block: B:8:0x001f  */
        static {
            AccessChecker accessChecker;
            if (JavaVersion.isJava9OrLater()) {
                try {
                    final Method declaredMethod = AccessibleObject.class.getDeclaredMethod("canAccess", Object.class);
                    accessChecker = new AccessChecker() { // from class: com.google.gson.internal.ReflectionAccessFilterHelper.AccessChecker.1
                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        {
                            super();
                        }

                        @Override // com.google.gson.internal.ReflectionAccessFilterHelper.AccessChecker
                        public boolean canAccess(AccessibleObject accessibleObject, Object obj) {
                            try {
                                return ((Boolean) declaredMethod.invoke(accessibleObject, obj)).booleanValue();
                            } catch (Exception e) {
                                throw new RuntimeException("Failed invoking canAccess", e);
                            }
                        }
                    };
                } catch (NoSuchMethodException unused) {
                }
                if (accessChecker == null) {
                    accessChecker = new AccessChecker() { // from class: com.google.gson.internal.ReflectionAccessFilterHelper.AccessChecker.2
                        @Override // com.google.gson.internal.ReflectionAccessFilterHelper.AccessChecker
                        public boolean canAccess(AccessibleObject accessibleObject, Object obj) {
                            return true;
                        }
                    };
                }
                INSTANCE = accessChecker;
            }
            accessChecker = null;
            if (accessChecker == null) {
            }
            INSTANCE = accessChecker;
        }
    }
}
