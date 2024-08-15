package j$.time.chrono;

import java.io.Serializable;
/* loaded from: classes2.dex */
public final class e extends a implements Serializable {
    public static final e a = new e();

    private e() {
    }

    public static boolean a(long j) {
        return (3 & j) == 0 && (j % 100 != 0 || j % 400 == 0);
    }
}
