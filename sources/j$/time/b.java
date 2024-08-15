package j$.time;

import java.io.Serializable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class b extends c implements Serializable {
    private final ZoneId a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(ZoneId zoneId) {
        this.a = zoneId;
    }

    public final ZoneId b() {
        return this.a;
    }

    public final boolean equals(Object obj) {
        if (obj instanceof b) {
            return this.a.equals(((b) obj).a);
        }
        return false;
    }

    public final int hashCode() {
        return this.a.hashCode() + 1;
    }

    public final String toString() {
        return "SystemClock[" + this.a + "]";
    }
}
