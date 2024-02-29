package j$.time;

import java.io.Serializable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class a extends b implements Serializable {
    private final ZoneId a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(ZoneId zoneId) {
        this.a = zoneId;
    }

    @Override // j$.time.b
    public long a() {
        return System.currentTimeMillis();
    }

    public boolean equals(Object obj) {
        if (obj instanceof a) {
            return this.a.equals(((a) obj).a);
        }
        return false;
    }

    public int hashCode() {
        return this.a.hashCode() + 1;
    }

    public String toString() {
        return "SystemClock[" + this.a + "]";
    }
}
