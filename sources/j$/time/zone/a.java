package j$.time.zone;

import j$.time.Instant;
import j$.time.ZoneOffset;
import j$.time.g;
import java.io.Serializable;

/* loaded from: classes2.dex */
public final class a implements Comparable, Serializable {
    private final g a;
    private final ZoneOffset b;
    private final ZoneOffset c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(long j, ZoneOffset zoneOffset, ZoneOffset zoneOffset2) {
        this.a = g.j(j, 0, zoneOffset);
        this.b = zoneOffset;
        this.c = zoneOffset2;
    }

    public final ZoneOffset a() {
        return this.c;
    }

    public final ZoneOffset b() {
        return this.b;
    }

    public final long c() {
        return this.a.k(this.b);
    }

    @Override // java.lang.Comparable
    public final int compareTo(Object obj) {
        a aVar = (a) obj;
        ZoneOffset zoneOffset = this.b;
        return Instant.l(this.a.k(zoneOffset), r1.m().h()).compareTo(Instant.l(aVar.a.k(aVar.b), r1.m().h()));
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof a)) {
            return false;
        }
        a aVar = (a) obj;
        return this.a.equals(aVar.a) && this.b.equals(aVar.b) && this.c.equals(aVar.c);
    }

    public final int hashCode() {
        return (this.a.hashCode() ^ this.b.hashCode()) ^ Integer.rotateLeft(this.c.hashCode(), 16);
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder("Transition[");
        ZoneOffset zoneOffset = this.c;
        int totalSeconds = zoneOffset.getTotalSeconds();
        ZoneOffset zoneOffset2 = this.b;
        sb.append(totalSeconds > zoneOffset2.getTotalSeconds() ? "Gap" : "Overlap");
        sb.append(" at ");
        sb.append(this.a);
        sb.append(zoneOffset2);
        sb.append(" to ");
        sb.append(zoneOffset);
        sb.append(']');
        return sb.toString();
    }
}
