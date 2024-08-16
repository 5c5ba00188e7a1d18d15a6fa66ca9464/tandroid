package j$.time.zone;

import j$.time.Instant;
import j$.time.LocalDate;
import j$.time.ZoneOffset;
import j$.time.g;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.Serializable;
import java.util.Arrays;
import java.util.TimeZone;
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
    private final transient ConcurrentHashMap g = new ConcurrentHashMap();

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
        long j2;
        Integer valueOf = Integer.valueOf(i2);
        ConcurrentHashMap concurrentHashMap = this.g;
        a[] aVarArr = (a[]) concurrentHashMap.get(valueOf);
        if (aVarArr != null) {
            return aVarArr;
        }
        TimeZone timeZone = this.f;
        if (timeZone == null) {
            b[] bVarArr = this.e;
            a[] aVarArr2 = new a[bVarArr.length];
            if (bVarArr.length > 0) {
                b bVar = bVarArr[0];
                throw null;
            }
            if (i2 < 2100) {
                concurrentHashMap.putIfAbsent(valueOf, aVarArr2);
            }
            return aVarArr2;
        }
        a[] aVarArr3 = j;
        if (i2 < 1800) {
            return aVarArr3;
        }
        long k = g.i(i2 - 1).k(this.b[0]);
        int offset = timeZone.getOffset(k * 1000);
        long j3 = 31968000 + k;
        while (k < j3) {
            long j4 = 7776000 + k;
            long j5 = k;
            if (offset != timeZone.getOffset(j4 * 1000)) {
                k = j5;
                while (j4 - k > 1) {
                    int i3 = offset;
                    long j6 = j3;
                    long j7 = j$.com.android.tools.r8.a.j(j4 + k, 2L);
                    if (timeZone.getOffset(j7 * 1000) == i3) {
                        k = j7;
                    } else {
                        j4 = j7;
                    }
                    offset = i3;
                    j3 = j6;
                }
                j2 = j3;
                int i4 = offset;
                if (timeZone.getOffset(k * 1000) == i4) {
                    k = j4;
                }
                ZoneOffset f = f(i4);
                offset = timeZone.getOffset(k * 1000);
                ZoneOffset f2 = f(offset);
                if (b(k, f2) == i2) {
                    aVarArr3 = (a[]) Arrays.copyOf(aVarArr3, aVarArr3.length + 1);
                    aVarArr3[aVarArr3.length - 1] = new a(k, f, f2);
                }
            } else {
                j2 = j3;
                k = j4;
            }
            j3 = j2;
        }
        if (1916 <= i2 && i2 < 2100) {
            concurrentHashMap.putIfAbsent(valueOf, aVarArr3);
        }
        return aVarArr3;
    }

    private static int b(long j2, ZoneOffset zoneOffset) {
        return LocalDate.p(j$.com.android.tools.r8.a.j(j2 + zoneOffset.getTotalSeconds(), 86400L)).m();
    }

    public static ZoneRules e(ZoneOffset zoneOffset) {
        j$.util.a.B(zoneOffset, "offset");
        return new ZoneRules(zoneOffset);
    }

    private static ZoneOffset f(int i2) {
        return ZoneOffset.j(i2 / 1000);
    }

    public final boolean c(Instant instant) {
        ZoneOffset zoneOffset;
        TimeZone timeZone = this.f;
        if (timeZone != null) {
            zoneOffset = f(timeZone.getRawOffset());
        } else {
            int length = this.c.length;
            ZoneOffset[] zoneOffsetArr = this.b;
            if (length == 0) {
                zoneOffset = zoneOffsetArr[0];
            } else {
                int binarySearch = Arrays.binarySearch(this.a, instant.i());
                if (binarySearch < 0) {
                    binarySearch = (-binarySearch) - 2;
                }
                zoneOffset = zoneOffsetArr[binarySearch + 1];
            }
        }
        return !zoneOffset.equals(getOffset(instant));
    }

    public final boolean d() {
        a aVar;
        TimeZone timeZone = this.f;
        if (timeZone == null) {
            return this.c.length == 0;
        } else if (timeZone.useDaylightTime() || timeZone.getDSTSavings() != 0) {
            return false;
        } else {
            Instant now = Instant.now();
            long i2 = now.i();
            if (now.j() > 0 && i2 < Long.MAX_VALUE) {
                i2++;
            }
            int b = b(i2, getOffset(now));
            a[] a = a(b);
            int length = a.length - 1;
            while (true) {
                if (length < 0) {
                    if (b > 1800) {
                        a[] a2 = a(b - 1);
                        int length2 = a2.length - 1;
                        while (true) {
                            if (length2 < 0) {
                                int offset = timeZone.getOffset((i2 - 1) * 1000);
                                long s = LocalDate.of(1800, 1, 1).s() * 86400;
                                for (long min = Math.min(i2 - 31104000, (j$.time.b.b().a() / 1000) + 31968000); s <= min; min -= 7776000) {
                                    int offset2 = timeZone.getOffset(min * 1000);
                                    if (offset != offset2) {
                                        int b2 = b(min, f(offset2));
                                        a[] a3 = a(b2 + 1);
                                        int length3 = a3.length - 1;
                                        while (true) {
                                            if (length3 < 0) {
                                                a[] a4 = a(b2);
                                                aVar = a4[a4.length - 1];
                                                break;
                                            } else if (i2 > a3[length3].c()) {
                                                aVar = a3[length3];
                                                break;
                                            } else {
                                                length3--;
                                            }
                                        }
                                    }
                                }
                            } else if (i2 > a2[length2].c()) {
                                aVar = a2[length2];
                                break;
                            } else {
                                length2--;
                            }
                        }
                    }
                    aVar = null;
                } else if (i2 > a[length].c()) {
                    aVar = a[length];
                    break;
                } else {
                    length--;
                }
            }
            return aVar == null;
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ZoneRules) {
            ZoneRules zoneRules = (ZoneRules) obj;
            return j$.util.a.A(this.f, zoneRules.f) && Arrays.equals(this.a, zoneRules.a) && Arrays.equals(this.b, zoneRules.b) && Arrays.equals(this.c, zoneRules.c) && Arrays.equals(this.d, zoneRules.d) && Arrays.equals(this.e, zoneRules.e);
        }
        return false;
    }

    public ZoneOffset getOffset(Instant instant) {
        TimeZone timeZone = this.f;
        if (timeZone != null) {
            return f(timeZone.getOffset(instant.m()));
        }
        long[] jArr = this.c;
        if (jArr.length == 0) {
            return this.b[0];
        }
        long i2 = instant.i();
        int length = this.e.length;
        ZoneOffset[] zoneOffsetArr = this.d;
        if (length <= 0 || i2 <= jArr[jArr.length - 1]) {
            int binarySearch = Arrays.binarySearch(jArr, i2);
            if (binarySearch < 0) {
                binarySearch = (-binarySearch) - 2;
            }
            return zoneOffsetArr[binarySearch + 1];
        }
        a[] a = a(b(i2, zoneOffsetArr[zoneOffsetArr.length - 1]));
        a aVar = null;
        for (int i3 = 0; i3 < a.length; i3++) {
            aVar = a[i3];
            if (i2 < aVar.c()) {
                return aVar.b();
            }
        }
        return aVar.a();
    }

    public final int hashCode() {
        TimeZone timeZone = this.f;
        return (((((timeZone != null ? timeZone.hashCode() : 0) ^ Arrays.hashCode(this.a)) ^ Arrays.hashCode(this.b)) ^ Arrays.hashCode(this.c)) ^ Arrays.hashCode(this.d)) ^ Arrays.hashCode(this.e);
    }

    public final String toString() {
        TimeZone timeZone = this.f;
        if (timeZone != null) {
            return "ZoneRules[timeZone=" + timeZone.getID() + "]";
        }
        StringBuilder sb = new StringBuilder("ZoneRules[currentStandardOffset=");
        ZoneOffset[] zoneOffsetArr = this.b;
        sb.append(zoneOffsetArr[zoneOffsetArr.length - 1]);
        sb.append("]");
        return sb.toString();
    }
}
