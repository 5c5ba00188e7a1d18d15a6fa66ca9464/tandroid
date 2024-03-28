package j$.time.zone;

import j$.time.Instant;
import j$.time.LocalDate;
import j$.time.ZoneOffset;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.Serializable;
import java.util.Arrays;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentMap;
/* loaded from: classes2.dex */
public final class ZoneRules implements Serializable {
    private static final long[] h = new long[0];
    private static final b[] i = new b[0];
    private static final a[] j = new a[0];
    private final long[] a;
    private final ZoneOffset[] b;
    private final long[] c;
    private final ZoneOffset[] d;
    private final b[] e;
    private final TimeZone f;
    private final transient ConcurrentMap g = new ConcurrentHashMap();

    private ZoneRules(ZoneOffset zoneOffset) {
        this.b = r0;
        ZoneOffset[] zoneOffsetArr = {zoneOffset};
        long[] jArr = h;
        this.a = jArr;
        this.c = jArr;
        this.d = zoneOffsetArr;
        this.e = i;
        this.f = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ZoneRules(TimeZone timeZone) {
        this.b = r0;
        ZoneOffset[] zoneOffsetArr = {f(timeZone.getRawOffset())};
        long[] jArr = h;
        this.a = jArr;
        this.c = jArr;
        this.d = zoneOffsetArr;
        this.e = i;
        this.f = timeZone;
    }

    private a[] a(int i2) {
        Integer valueOf = Integer.valueOf(i2);
        a[] aVarArr = (a[]) this.g.get(valueOf);
        if (aVarArr != null) {
            return aVarArr;
        }
        if (this.f == null) {
            b[] bVarArr = this.e;
            a[] aVarArr2 = new a[bVarArr.length];
            if (bVarArr.length > 0) {
                b bVar = bVarArr[0];
                throw null;
            }
            if (i2 < 2100) {
                this.g.putIfAbsent(valueOf, aVarArr2);
            }
            return aVarArr2;
        } else if (i2 < 1800) {
            return j;
        } else {
            long l = j$.time.e.j(i2 - 1, 12, 31, 0, 0).l(this.b[0]);
            long j2 = 1000;
            int offset = this.f.getOffset(l * 1000);
            long j3 = 31968000 + l;
            a[] aVarArr3 = j;
            while (l < j3) {
                long j4 = 7776000 + l;
                long j5 = l;
                if (offset != this.f.getOffset(j4 * j2)) {
                    l = j5;
                    while (j4 - l > 1) {
                        long j6 = j3;
                        long d = j$.lang.d.d(j4 + l, 2L);
                        long j7 = j4;
                        if (this.f.getOffset(d * 1000) == offset) {
                            l = d;
                            j2 = 1000;
                            j4 = j7;
                        } else {
                            j4 = d;
                            j2 = 1000;
                        }
                        j3 = j6;
                    }
                    long j8 = j3;
                    long j9 = j4;
                    long j10 = j2;
                    if (this.f.getOffset(l * j10) == offset) {
                        l = j9;
                    }
                    ZoneOffset f = f(offset);
                    int offset2 = this.f.getOffset(l * j10);
                    ZoneOffset f2 = f(offset2);
                    if (b(l, f2) == i2) {
                        aVarArr3 = (a[]) Arrays.copyOf(aVarArr3, aVarArr3.length + 1);
                        aVarArr3[aVarArr3.length - 1] = new a(l, f, f2);
                    }
                    offset = offset2;
                    j2 = j10;
                    j3 = j8;
                } else {
                    l = j4;
                }
            }
            if (1916 <= i2 && i2 < 2100) {
                this.g.putIfAbsent(valueOf, aVarArr3);
            }
            return aVarArr3;
        }
    }

    private int b(long j2, ZoneOffset zoneOffset) {
        return LocalDate.p(j$.lang.d.d(j2 + zoneOffset.getTotalSeconds(), 86400L)).m();
    }

    public static ZoneRules e(ZoneOffset zoneOffset) {
        return new ZoneRules(zoneOffset);
    }

    private static ZoneOffset f(int i2) {
        return ZoneOffset.j(i2 / 1000);
    }

    public boolean c(Instant instant) {
        ZoneOffset zoneOffset;
        TimeZone timeZone = this.f;
        if (timeZone != null) {
            zoneOffset = f(timeZone.getRawOffset());
        } else if (this.c.length == 0) {
            zoneOffset = this.b[0];
        } else {
            int binarySearch = Arrays.binarySearch(this.a, instant.i());
            if (binarySearch < 0) {
                binarySearch = (-binarySearch) - 2;
            }
            zoneOffset = this.b[binarySearch + 1];
        }
        return !zoneOffset.equals(getOffset(instant));
    }

    public boolean d() {
        TimeZone timeZone = this.f;
        if (timeZone == null) {
            return this.c.length == 0;
        } else if (timeZone.useDaylightTime() || this.f.getDSTSavings() != 0) {
            return false;
        } else {
            Instant now = Instant.now();
            a aVar = null;
            if (this.f != null) {
                long i2 = now.i();
                if (now.j() > 0 && i2 < Long.MAX_VALUE) {
                    i2++;
                }
                int b = b(i2, getOffset(now));
                a[] a = a(b);
                int length = a.length - 1;
                while (true) {
                    if (length >= 0) {
                        if (i2 > a[length].d()) {
                            aVar = a[length];
                            break;
                        }
                        length--;
                    } else if (b > 1800) {
                        a[] a2 = a(b - 1);
                        int length2 = a2.length - 1;
                        while (true) {
                            if (length2 < 0) {
                                long min = Math.min(i2 - 31104000, (j$.time.b.b().a() / 1000) + 31968000);
                                int offset = this.f.getOffset((i2 - 1) * 1000);
                                long s = LocalDate.of(1800, 1, 1).s() * 86400;
                                while (true) {
                                    if (s > min) {
                                        break;
                                    }
                                    int offset2 = this.f.getOffset(min * 1000);
                                    if (offset != offset2) {
                                        int b2 = b(min, f(offset2));
                                        a[] a3 = a(b2 + 1);
                                        int length3 = a3.length - 1;
                                        while (true) {
                                            if (length3 < 0) {
                                                a[] a4 = a(b2);
                                                aVar = a4[a4.length - 1];
                                                break;
                                            } else if (i2 > a3[length3].d()) {
                                                aVar = a3[length3];
                                                break;
                                            } else {
                                                length3--;
                                            }
                                        }
                                    } else {
                                        min -= 7776000;
                                    }
                                }
                            } else if (i2 > a2[length2].d()) {
                                aVar = a2[length2];
                                break;
                            } else {
                                length2--;
                            }
                        }
                    }
                }
            } else if (this.c.length != 0) {
                long i3 = now.i();
                if (now.j() > 0 && i3 < Long.MAX_VALUE) {
                    i3++;
                }
                long[] jArr = this.c;
                long j2 = jArr[jArr.length - 1];
                if (this.e.length > 0 && i3 > j2) {
                    ZoneOffset[] zoneOffsetArr = this.d;
                    ZoneOffset zoneOffset = zoneOffsetArr[zoneOffsetArr.length - 1];
                    int b3 = b(i3, zoneOffset);
                    a[] a5 = a(b3);
                    int length4 = a5.length - 1;
                    while (true) {
                        if (length4 < 0) {
                            int i4 = b3 - 1;
                            if (i4 > b(j2, zoneOffset)) {
                                a[] a6 = a(i4);
                                aVar = a6[a6.length - 1];
                            }
                        } else if (i3 > a5[length4].d()) {
                            aVar = a5[length4];
                            break;
                        } else {
                            length4--;
                        }
                    }
                }
                int binarySearch = Arrays.binarySearch(this.c, i3);
                if (binarySearch < 0) {
                    binarySearch = (-binarySearch) - 1;
                }
                if (binarySearch > 0) {
                    int i5 = binarySearch - 1;
                    long j3 = this.c[i5];
                    ZoneOffset[] zoneOffsetArr2 = this.d;
                    aVar = new a(j3, zoneOffsetArr2[i5], zoneOffsetArr2[binarySearch]);
                }
            }
            return aVar == null;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ZoneRules) {
            ZoneRules zoneRules = (ZoneRules) obj;
            return j$.util.a.u(this.f, zoneRules.f) && Arrays.equals(this.a, zoneRules.a) && Arrays.equals(this.b, zoneRules.b) && Arrays.equals(this.c, zoneRules.c) && Arrays.equals(this.d, zoneRules.d) && Arrays.equals(this.e, zoneRules.e);
        }
        return false;
    }

    public ZoneOffset getOffset(Instant instant) {
        long[] jArr;
        ZoneOffset[] zoneOffsetArr;
        TimeZone timeZone = this.f;
        if (timeZone != null) {
            return f(timeZone.getOffset(instant.m()));
        }
        if (this.c.length == 0) {
            return this.b[0];
        }
        long i2 = instant.i();
        if (this.e.length > 0) {
            if (i2 > this.c[jArr.length - 1]) {
                a[] a = a(b(i2, this.d[zoneOffsetArr.length - 1]));
                a aVar = null;
                for (int i3 = 0; i3 < a.length; i3++) {
                    aVar = a[i3];
                    if (i2 < aVar.d()) {
                        return aVar.c();
                    }
                }
                return aVar.b();
            }
        }
        int binarySearch = Arrays.binarySearch(this.c, i2);
        if (binarySearch < 0) {
            binarySearch = (-binarySearch) - 2;
        }
        return this.d[binarySearch + 1];
    }

    public int hashCode() {
        TimeZone timeZone = this.f;
        return (((((timeZone != null ? timeZone.hashCode() : 0) ^ Arrays.hashCode(this.a)) ^ Arrays.hashCode(this.b)) ^ Arrays.hashCode(this.c)) ^ Arrays.hashCode(this.d)) ^ Arrays.hashCode(this.e);
    }

    public String toString() {
        StringBuilder sb;
        if (this.f != null) {
            sb = new StringBuilder();
            sb.append("ZoneRules[timeZone=");
            sb.append(this.f.getID());
        } else {
            sb = new StringBuilder();
            sb.append("ZoneRules[currentStandardOffset=");
            ZoneOffset[] zoneOffsetArr = this.b;
            sb.append(zoneOffsetArr[zoneOffsetArr.length - 1]);
        }
        sb.append("]");
        return sb.toString();
    }
}
