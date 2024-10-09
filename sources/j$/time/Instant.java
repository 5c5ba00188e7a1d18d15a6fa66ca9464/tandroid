package j$.time;

import j$.time.temporal.q;
import java.io.Serializable;
import org.telegram.messenger.MediaController;

/* loaded from: classes2.dex */
public final class Instant implements j$.time.temporal.k, Comparable<Instant>, Serializable {
    public static final Instant c = new Instant(0, 0);
    private final long a;
    private final int b;

    static {
        l(-31557014167219200L, 0L);
        l(31556889864403199L, 999999999L);
    }

    private Instant(long j, int i) {
        this.a = j;
        this.b = i;
    }

    private static Instant g(long j, int i) {
        if ((i | j) == 0) {
            return c;
        }
        if (j < -31557014167219200L || j > 31556889864403199L) {
            throw new c("Instant exceeds minimum or maximum instant");
        }
        return new Instant(j, i);
    }

    public static Instant h(j$.time.temporal.k kVar) {
        if (kVar instanceof Instant) {
            return (Instant) kVar;
        }
        j$.util.a.B(kVar, "temporal");
        try {
            return l(kVar.b(j$.time.temporal.a.INSTANT_SECONDS), kVar.d(j$.time.temporal.a.NANO_OF_SECOND));
        } catch (c e) {
            throw new c("Unable to obtain Instant from TemporalAccessor: " + kVar + " of type " + kVar.getClass().getName(), e);
        }
    }

    public static Instant k(long j) {
        return g(j$.com.android.tools.r8.a.j(j, 1000L), ((int) j$.com.android.tools.r8.a.i(j, 1000L)) * MediaController.VIDEO_BITRATE_480);
    }

    public static Instant l(long j, long j2) {
        return g(j$.com.android.tools.r8.a.g(j, j$.com.android.tools.r8.a.j(j2, 1000000000L)), (int) j$.com.android.tools.r8.a.i(j2, 1000000000L));
    }

    public static Instant now() {
        b.b();
        return k(System.currentTimeMillis());
    }

    @Override // j$.time.temporal.k
    public final q a(j$.time.temporal.l lVar) {
        return j$.time.temporal.j.c(this, lVar);
    }

    @Override // j$.time.temporal.k
    public final long b(j$.time.temporal.l lVar) {
        int i;
        if (!(lVar instanceof j$.time.temporal.a)) {
            return lVar.b(this);
        }
        int i2 = e.a[((j$.time.temporal.a) lVar).ordinal()];
        int i3 = this.b;
        if (i2 == 1) {
            return i3;
        }
        if (i2 == 2) {
            i = i3 / 1000;
        } else {
            if (i2 != 3) {
                if (i2 == 4) {
                    return this.a;
                }
                throw new j$.time.temporal.p("Unsupported field: " + lVar);
            }
            i = i3 / MediaController.VIDEO_BITRATE_480;
        }
        return i;
    }

    @Override // j$.time.temporal.k
    public final Object c(j$.time.temporal.n nVar) {
        if (nVar == j$.time.temporal.j.h()) {
            return j$.time.temporal.b.NANOS;
        }
        if (nVar == j$.time.temporal.j.d() || nVar == j$.time.temporal.j.j() || nVar == j$.time.temporal.j.i() || nVar == j$.time.temporal.j.g() || nVar == j$.time.temporal.j.e() || nVar == j$.time.temporal.j.f()) {
            return null;
        }
        return nVar.a(this);
    }

    @Override // j$.time.temporal.k
    public final int d(j$.time.temporal.a aVar) {
        if (!(aVar instanceof j$.time.temporal.a)) {
            q c2 = j$.time.temporal.j.c(this, aVar);
            aVar.getClass();
            return c2.a(b(aVar), aVar);
        }
        int i = e.a[aVar.ordinal()];
        int i2 = this.b;
        if (i == 1) {
            return i2;
        }
        if (i == 2) {
            return i2 / 1000;
        }
        if (i == 3) {
            return i2 / MediaController.VIDEO_BITRATE_480;
        }
        if (i == 4) {
            j$.time.temporal.a.INSTANT_SECONDS.f(this.a);
        }
        throw new j$.time.temporal.p("Unsupported field: " + aVar);
    }

    @Override // j$.time.temporal.k
    public final boolean e(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.INSTANT_SECONDS || lVar == j$.time.temporal.a.NANO_OF_SECOND || lVar == j$.time.temporal.a.MICRO_OF_SECOND || lVar == j$.time.temporal.a.MILLI_OF_SECOND : lVar != null && lVar.c(this);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Instant)) {
            return false;
        }
        Instant instant = (Instant) obj;
        return this.a == instant.a && this.b == instant.b;
    }

    @Override // java.lang.Comparable
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public final int compareTo(Instant instant) {
        int compare = Long.compare(this.a, instant.a);
        return compare != 0 ? compare : this.b - instant.b;
    }

    public final int hashCode() {
        long j = this.a;
        return (this.b * 51) + ((int) (j ^ (j >>> 32)));
    }

    public final long i() {
        return this.a;
    }

    public final int j() {
        return this.b;
    }

    public final long m() {
        long f;
        int i;
        int i2 = this.b;
        long j = this.a;
        if (j >= 0 || i2 <= 0) {
            f = j$.com.android.tools.r8.a.f(j);
            i = i2 / MediaController.VIDEO_BITRATE_480;
        } else {
            f = j$.com.android.tools.r8.a.f(j + 1);
            i = (i2 / MediaController.VIDEO_BITRATE_480) - 1000;
        }
        return j$.com.android.tools.r8.a.g(f, i);
    }

    public final String toString() {
        return j$.time.format.a.f.a(this);
    }
}
