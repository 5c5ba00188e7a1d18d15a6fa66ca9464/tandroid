package androidx.core.app;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
/* loaded from: classes.dex */
public final class AppOpsManagerCompat {
    public static String permissionToOp(String str) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.permissionToOp(str);
        }
        return null;
    }

    public static int noteProxyOpNoThrow(Context context, String str, String str2) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.noteProxyOpNoThrow((AppOpsManager) Api23Impl.getSystemService(context, AppOpsManager.class), str, str2);
        }
        return 1;
    }

    public static int checkOrNoteProxyOp(Context context, int i, String str, String str2) {
        if (Build.VERSION.SDK_INT >= 29) {
            AppOpsManager systemService = Api29Impl.getSystemService(context);
            int checkOpNoThrow = Api29Impl.checkOpNoThrow(systemService, str, Binder.getCallingUid(), str2);
            return checkOpNoThrow != 0 ? checkOpNoThrow : Api29Impl.checkOpNoThrow(systemService, str, i, Api29Impl.getOpPackageName(context));
        }
        return noteProxyOpNoThrow(context, str, str2);
    }

    /* loaded from: classes.dex */
    static class Api29Impl {
        static AppOpsManager getSystemService(Context context) {
            Object systemService;
            systemService = context.getSystemService(AppOpsManager.class);
            return (AppOpsManager) systemService;
        }

        static int checkOpNoThrow(AppOpsManager appOpsManager, String str, int i, String str2) {
            if (appOpsManager == null) {
                return 1;
            }
            return appOpsManager.checkOpNoThrow(str, i, str2);
        }

        static String getOpPackageName(Context context) {
            String opPackageName;
            opPackageName = context.getOpPackageName();
            return opPackageName;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Api23Impl {
        static String permissionToOp(String str) {
            String permissionToOp;
            permissionToOp = AppOpsManager.permissionToOp(str);
            return permissionToOp;
        }

        static <T> T getSystemService(Context context, Class<T> cls) {
            Object systemService;
            systemService = context.getSystemService(cls);
            return (T) systemService;
        }

        static int noteProxyOp(AppOpsManager appOpsManager, String str, String str2) {
            int noteProxyOp;
            noteProxyOp = appOpsManager.noteProxyOp(str, str2);
            return noteProxyOp;
        }

        static int noteProxyOpNoThrow(AppOpsManager appOpsManager, String str, String str2) {
            int noteProxyOpNoThrow;
            noteProxyOpNoThrow = appOpsManager.noteProxyOpNoThrow(str, str2);
            return noteProxyOpNoThrow;
        }
    }
}
