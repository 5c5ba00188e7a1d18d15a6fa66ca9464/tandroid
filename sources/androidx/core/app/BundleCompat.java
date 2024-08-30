package androidx.core.app;

import android.os.Bundle;
import android.os.IBinder;
/* loaded from: classes.dex */
public abstract class BundleCompat {

    /* loaded from: classes.dex */
    static class Api18Impl {
        static IBinder getBinder(Bundle bundle, String str) {
            return bundle.getBinder(str);
        }

        static void putBinder(Bundle bundle, String str, IBinder iBinder) {
            bundle.putBinder(str, iBinder);
        }
    }

    public static void putBinder(Bundle bundle, String str, IBinder iBinder) {
        Api18Impl.putBinder(bundle, str, iBinder);
    }
}
