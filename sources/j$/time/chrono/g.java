package j$.time.chrono;

import java.io.Serializable;

/* loaded from: classes2.dex */
public final class g extends a implements Serializable {
    public static final g a = new g();

    private g() {
    }

    public static boolean a(long j) {
        return (3 & j) == 0 && (j % 100 != 0 || j % 400 == 0);
    }
}
