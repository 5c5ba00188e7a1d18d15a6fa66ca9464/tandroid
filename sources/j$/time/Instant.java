package j$.time;

import j$.time.temporal.p;
import j$.time.temporal.q;
import java.io.Serializable;
import org.telegram.messenger.MediaController;
/* loaded from: classes2.dex */
public final class Instant implements j$.time.temporal.k, Comparable<Instant>, Serializable {
    public static final Instant c = new Instant(0, 0);
    private final long a;
    private final int b;

    static {
        k(-31557014167219200L, 0L);
        k(31556889864403199L, 999999999L);
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
            throw new d("Instant exceeds minimum or maximum instant");
        }
        return new Instant(j, i);
    }

    public static Instant h(j$.time.temporal.k kVar) {
        if (kVar instanceof Instant) {
            return (Instant) kVar;
        }
        if (kVar != null) {
            try {
                return k(kVar.c(j$.time.temporal.a.INSTANT_SECONDS), kVar.e(j$.time.temporal.a.NANO_OF_SECOND));
            } catch (d e) {
                throw new d("Unable to obtain Instant from TemporalAccessor: " + kVar + " of type " + kVar.getClass().getName(), e);
            }
        }
        throw new NullPointerException("temporal");
    }

    public static Instant j(long j) {
        return g(a.f(j, 1000L), ((int) a.d(j, 1000L)) * MediaController.VIDEO_BITRATE_480);
    }

    public static Instant k(long j, long j2) {
        return g(a.c(j, a.f(j2, 1000000000L)), (int) a.d(j2, 1000000000L));
    }

    public static Instant now() {
        c.a();
        return j(System.currentTimeMillis());
    }

    @Override // j$.time.temporal.k
    public final q a(j$.time.temporal.l lVar) {
        return j$.time.temporal.j.c(this, lVar);
    }

    @Override // j$.time.temporal.k
    public final boolean b(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.INSTANT_SECONDS || lVar == j$.time.temporal.a.NANO_OF_SECOND || lVar == j$.time.temporal.a.MICRO_OF_SECOND || lVar == j$.time.temporal.a.MILLI_OF_SECOND : lVar != null && lVar.a(this);
    }

    @Override // j$.time.temporal.k
    public final long c(j$.time.temporal.l lVar) {
        int i;
        if (lVar instanceof j$.time.temporal.a) {
            int i2 = f.a[((j$.time.temporal.a) lVar).ordinal()];
            int i3 = this.b;
            if (i2 != 1) {
                if (i2 == 2) {
                    i = i3 / 1000;
                } else if (i2 != 3) {
                    if (i2 == 4) {
                        return this.a;
                    }
                    throw new p("Unsupported field: " + lVar);
                } else {
                    i = i3 / MediaController.VIDEO_BITRATE_480;
                }
                return i;
            }
            return i3;
        }
        return lVar.d(this);
    }

    @Override // j$.time.temporal.k
    public final Object d(j$.time.temporal.n nVar) {
        if (nVar == j$.time.temporal.j.h()) {
            return j$.time.temporal.b.NANOS;
        }
        if (nVar == j$.time.temporal.j.d() || nVar == j$.time.temporal.j.j() || nVar == j$.time.temporal.j.i() || nVar == j$.time.temporal.j.g() || nVar == j$.time.temporal.j.e() || nVar == j$.time.temporal.j.f()) {
            return null;
        }
        return nVar.a(this);
    }

    @Override // j$.time.temporal.k
    public final int e(j$.time.temporal.a aVar) {
        if (aVar instanceof j$.time.temporal.a) {
            int i = f.a[aVar.ordinal()];
            int i2 = this.b;
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        if (i == 4) {
                            j$.time.temporal.a.INSTANT_SECONDS.e(this.a);
                        }
                        throw new p("Unsupported field: " + aVar);
                    }
                    return i2 / MediaController.VIDEO_BITRATE_480;
                }
                return i2 / 1000;
            }
            return i2;
        }
        return j$.time.temporal.j.c(this, aVar).a(c(aVar), aVar);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Instant) {
            Instant instant = (Instant) obj;
            return this.a == instant.a && this.b == instant.b;
        }
        return false;
    }

    @Override // java.lang.Comparable
    /* renamed from: f */
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

    public final long l() {
        long e;
        int i;
        int i2 = this.b;
        long j = this.a;
        if (j >= 0 || i2 <= 0) {
            e = a.e(j);
            i = i2 / MediaController.VIDEO_BITRATE_480;
        } else {
            e = a.e(j + 1);
            i = (i2 / MediaController.VIDEO_BITRATE_480) - 1000;
        }
        return a.c(e, i);
    }

    public final String toString() {
        return j$.time.format.b.d.a(this);
    }
}
