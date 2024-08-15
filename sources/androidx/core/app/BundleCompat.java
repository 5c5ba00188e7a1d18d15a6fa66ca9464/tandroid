package androidx.core.app;

import android.os.Bundle;
import android.os.IBinder;
/* loaded from: classes.dex */
public final class BundleCompat {
    public static void putBinder(Bundle bundle, String str, IBinder iBinder) {
        Api18Impl.putBinder(bundle, str, iBinder);
    }

    /* loaded from: classes.dex */
    static class Api18Impl {
        static IBinder getBinder(Bundle bundle, String str) {
            return bundle.getBinder(str);
        }

        static void putBinder(Bundle bundle, String str, IBinder iBinder) {
            bundle.putBinder(str, iBinder);
        }
    }
}
