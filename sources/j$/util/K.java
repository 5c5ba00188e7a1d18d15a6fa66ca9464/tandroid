package j$.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
/* loaded from: classes2.dex */
abstract class K {
    static final boolean a = ((Boolean) AccessController.doPrivileged(new PrivilegedAction() { // from class: j$.util.J
        @Override // java.security.PrivilegedAction
        public final Object run() {
            boolean z = K.a;
            return Boolean.valueOf(Boolean.getBoolean("org.openjdk.java.util.stream.tripwire"));
        }
    })).booleanValue();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Class cls, String str) {
        throw new UnsupportedOperationException(cls + " tripwire tripped but logging not supported: " + str);
    }
}
