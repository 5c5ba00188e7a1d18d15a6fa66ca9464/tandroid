package j$.util;

import java.security.PrivilegedAction;
/* loaded from: classes2.dex */
public final /* synthetic */ class K implements PrivilegedAction {
    public static final /* synthetic */ K a = new K();

    private /* synthetic */ K() {
    }

    @Override // java.security.PrivilegedAction
    public final Object run() {
        boolean z = L.a;
        return Boolean.valueOf(Boolean.getBoolean("org.openjdk.java.util.stream.tripwire"));
    }
}
