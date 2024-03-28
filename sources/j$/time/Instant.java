package j$.time;

import j$.time.temporal.n;
import j$.time.temporal.o;
import j$.time.temporal.p;
import j$.time.temporal.q;
import j$.time.temporal.r;
import j$.time.temporal.s;
import j$.time.temporal.t;
import j$.time.temporal.u;
import j$.time.temporal.w;
import j$.time.temporal.x;
import java.io.Serializable;
import org.telegram.messenger.MediaController;
/* loaded from: classes2.dex */
public final class Instant implements Comparable<Instant>, Serializable, Serializable {
    public static final Instant c = new Instant(0, 0);
    private final long a;
    private final int b;

    /* loaded from: classes2.dex */
    static /* synthetic */ class a {
        static final /* synthetic */ int[] a;
        static final /* synthetic */ int[] b;

        static {
            int[] iArr = new int[j$.time.temporal.b.values().length];
            b = iArr;
            try {
                iArr[j$.time.temporal.b.NANOS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                b[j$.time.temporal.b.MICROS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                b[j$.time.temporal.b.MILLIS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                b[j$.time.temporal.b.SECONDS.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                b[j$.time.temporal.b.MINUTES.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                b[j$.time.temporal.b.HOURS.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                b[j$.time.temporal.b.HALF_DAYS.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                b[j$.time.temporal.b.DAYS.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            int[] iArr2 = new int[j$.time.temporal.a.values().length];
            a = iArr2;
            try {
                iArr2[j$.time.temporal.a.NANO_OF_SECOND.ordinal()] = 1;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                a[j$.time.temporal.a.MICRO_OF_SECOND.ordinal()] = 2;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                a[j$.time.temporal.a.MILLI_OF_SECOND.ordinal()] = 3;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                a[j$.time.temporal.a.INSTANT_SECONDS.ordinal()] = 4;
            } catch (NoSuchFieldError unused12) {
            }
        }
    }

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
        try {
            return l(kVar.c(j$.time.temporal.a.INSTANT_SECONDS), kVar.a(j$.time.temporal.a.NANO_OF_SECOND));
        } catch (c e) {
            throw new c("Unable to obtain Instant from TemporalAccessor: " + kVar + " of type " + kVar.getClass().getName(), e);
        }
    }

    public static Instant k(long j) {
        return g(j$.lang.d.d(j, 1000L), ((int) j$.lang.d.c(j, 1000L)) * MediaController.VIDEO_BITRATE_480);
    }

    public static Instant l(long j, long j2) {
        return g(j$.lang.d.b(j, j$.lang.d.d(j2, 1000000000L)), (int) j$.lang.d.c(j2, 1000000000L));
    }

    public static Instant now() {
        ZoneOffset zoneOffset = ZoneOffset.f;
        return k(System.currentTimeMillis());
    }

    public int a(j$.time.temporal.l lVar) {
        if (lVar instanceof j$.time.temporal.a) {
            int i = a.a[((j$.time.temporal.a) lVar).ordinal()];
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        if (i == 4) {
                            j$.time.temporal.a.INSTANT_SECONDS.g(this.a);
                        }
                        throw new w("Unsupported field: " + lVar);
                    }
                    return this.b / MediaController.VIDEO_BITRATE_480;
                }
                return this.b / 1000;
            }
            return this.b;
        }
        return j$.time.temporal.j.c(this, lVar).a(lVar.c(this), lVar);
    }

    public x b(j$.time.temporal.l lVar) {
        return j$.time.temporal.j.c(this, lVar);
    }

    public long c(j$.time.temporal.l lVar) {
        int i;
        if (lVar instanceof j$.time.temporal.a) {
            int i2 = a.a[((j$.time.temporal.a) lVar).ordinal()];
            if (i2 == 1) {
                i = this.b;
            } else if (i2 == 2) {
                i = this.b / 1000;
            } else if (i2 != 3) {
                if (i2 == 4) {
                    return this.a;
                }
                throw new w("Unsupported field: " + lVar);
            } else {
                i = this.b / MediaController.VIDEO_BITRATE_480;
            }
            return i;
        }
        return lVar.c(this);
    }

    @Override // java.lang.Comparable
    public int compareTo(Instant instant) {
        Instant instant2 = instant;
        int compare = Long.compare(this.a, instant2.a);
        return compare != 0 ? compare : this.b - instant2.b;
    }

    public Object d(u uVar) {
        int i = t.a;
        if (uVar == o.a) {
            return j$.time.temporal.b.NANOS;
        }
        if (uVar == n.a || uVar == j$.time.temporal.m.a || uVar == q.a || uVar == p.a || uVar == r.a || uVar == s.a) {
            return null;
        }
        return uVar.a(this);
    }

    public boolean e(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.INSTANT_SECONDS || lVar == j$.time.temporal.a.NANO_OF_SECOND || lVar == j$.time.temporal.a.MICRO_OF_SECOND || lVar == j$.time.temporal.a.MILLI_OF_SECOND : lVar != null && lVar.d(this);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Instant) {
            Instant instant = (Instant) obj;
            return this.a == instant.a && this.b == instant.b;
        }
        return false;
    }

    public int f(Instant instant) {
        int compare = Long.compare(this.a, instant.a);
        return compare != 0 ? compare : this.b - instant.b;
    }

    public int hashCode() {
        long j = this.a;
        return (this.b * 51) + ((int) (j ^ (j >>> 32)));
    }

    public long i() {
        return this.a;
    }

    public int j() {
        return this.b;
    }

    public long m() {
        long e;
        int i;
        long j = this.a;
        if (j >= 0 || this.b <= 0) {
            e = j$.lang.d.e(j, 1000L);
            i = this.b / MediaController.VIDEO_BITRATE_480;
        } else {
            e = j$.lang.d.e(j + 1, 1000L);
            i = (this.b / MediaController.VIDEO_BITRATE_480) - 1000;
        }
        return j$.lang.d.b(e, i);
    }

    public String toString() {
        return j$.time.format.a.f.a(this);
    }
}
