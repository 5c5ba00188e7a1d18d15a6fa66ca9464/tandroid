package j$.time;

import j$.time.temporal.p;
import j$.time.temporal.q;
import j$.time.zone.ZoneRules;
import j$.util.concurrent.ConcurrentHashMap;
/* loaded from: classes2.dex */
public final class ZoneOffset extends ZoneId implements j$.time.temporal.k, Comparable<ZoneOffset> {
    private static final ConcurrentHashMap d = new ConcurrentHashMap(16, 0.75f, 4);
    private static final ConcurrentHashMap e = new ConcurrentHashMap(16, 0.75f, 4);
    public static final ZoneOffset f = i(0);
    private final int b;
    private final transient String c;

    static {
        i(-64800);
        i(64800);
    }

    private ZoneOffset(int i) {
        String sb;
        this.b = i;
        if (i == 0) {
            sb = "Z";
        } else {
            int abs = Math.abs(i);
            StringBuilder sb2 = new StringBuilder();
            int i2 = abs / 3600;
            int i3 = (abs / 60) % 60;
            sb2.append(i < 0 ? "-" : "+");
            sb2.append(i2 < 10 ? "0" : "");
            sb2.append(i2);
            sb2.append(i3 < 10 ? ":0" : ":");
            sb2.append(i3);
            int i4 = abs % 60;
            if (i4 != 0) {
                sb2.append(i4 >= 10 ? ":" : ":0");
                sb2.append(i4);
            }
            sb = sb2.toString();
        }
        this.c = sb;
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x008b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00a4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ZoneOffset g(String str) {
        int j;
        int i;
        int i2;
        char charAt;
        if (str == null) {
            throw new NullPointerException("offsetId");
        }
        ZoneOffset zoneOffset = (ZoneOffset) e.get(str);
        if (zoneOffset != null) {
            return zoneOffset;
        }
        int length = str.length();
        if (length == 2) {
            str = str.charAt(0) + "0" + str.charAt(1);
        } else if (length != 3) {
            if (length == 5) {
                j = j(str, 1, false);
                i = j(str, 3, false);
            } else if (length != 6) {
                if (length == 7) {
                    j = j(str, 1, false);
                    i = j(str, 3, false);
                    i2 = j(str, 5, false);
                } else if (length != 9) {
                    throw new d("Invalid ID for ZoneOffset, invalid format: ".concat(str));
                } else {
                    j = j(str, 1, false);
                    i = j(str, 4, true);
                    i2 = j(str, 7, true);
                }
                charAt = str.charAt(0);
                if (charAt != '+' || charAt == '-') {
                    return charAt == '-' ? h(-j, -i, -i2) : h(j, i, i2);
                }
                throw new d("Invalid ID for ZoneOffset, plus/minus not found when expected: ".concat(str));
            } else {
                j = j(str, 1, false);
                i = j(str, 4, true);
            }
            i2 = 0;
            charAt = str.charAt(0);
            if (charAt != '+') {
            }
            if (charAt == '-') {
            }
        }
        j = j(str, 1, false);
        i = 0;
        i2 = 0;
        charAt = str.charAt(0);
        if (charAt != '+') {
        }
        if (charAt == '-') {
        }
    }

    public static ZoneOffset h(int i, int i2, int i3) {
        if (i < -18 || i > 18) {
            throw new d("Zone offset hours not in valid range: value " + i + " is not in the range -18 to 18");
        }
        if (i > 0) {
            if (i2 < 0 || i3 < 0) {
                throw new d("Zone offset minutes and seconds must be positive because hours is positive");
            }
        } else if (i < 0) {
            if (i2 > 0 || i3 > 0) {
                throw new d("Zone offset minutes and seconds must be negative because hours is negative");
            }
        } else if ((i2 > 0 && i3 < 0) || (i2 < 0 && i3 > 0)) {
            throw new d("Zone offset minutes and seconds must have the same sign");
        }
        if (i2 < -59 || i2 > 59) {
            throw new d("Zone offset minutes not in valid range: value " + i2 + " is not in the range -59 to 59");
        } else if (i3 < -59 || i3 > 59) {
            throw new d("Zone offset seconds not in valid range: value " + i3 + " is not in the range -59 to 59");
        } else if (Math.abs(i) != 18 || (i2 | i3) == 0) {
            return i((i2 * 60) + (i * 3600) + i3);
        } else {
            throw new d("Zone offset not in valid range: -18:00 to +18:00");
        }
    }

    public static ZoneOffset i(int i) {
        if (i < -64800 || i > 64800) {
            throw new d("Zone offset not in valid range: -18:00 to +18:00");
        }
        if (i % 900 == 0) {
            Integer valueOf = Integer.valueOf(i);
            ConcurrentHashMap concurrentHashMap = d;
            ZoneOffset zoneOffset = (ZoneOffset) concurrentHashMap.get(valueOf);
            if (zoneOffset == null) {
                concurrentHashMap.putIfAbsent(valueOf, new ZoneOffset(i));
                ZoneOffset zoneOffset2 = (ZoneOffset) concurrentHashMap.get(valueOf);
                e.putIfAbsent(zoneOffset2.c, zoneOffset2);
                return zoneOffset2;
            }
            return zoneOffset;
        }
        return new ZoneOffset(i);
    }

    private static int j(CharSequence charSequence, int i, boolean z) {
        if (z && charSequence.charAt(i - 1) != ':') {
            throw new d("Invalid ID for ZoneOffset, colon not found when expected: " + ((Object) charSequence));
        }
        char charAt = charSequence.charAt(i);
        char charAt2 = charSequence.charAt(i + 1);
        if (charAt >= '0' && charAt <= '9' && charAt2 >= '0' && charAt2 <= '9') {
            return (charAt2 - '0') + ((charAt - '0') * 10);
        }
        throw new d("Invalid ID for ZoneOffset, non numeric characters found: " + ((Object) charSequence));
    }

    @Override // j$.time.temporal.k
    public final q a(j$.time.temporal.l lVar) {
        return j$.time.temporal.j.c(this, lVar);
    }

    @Override // j$.time.temporal.k
    public final boolean b(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.OFFSET_SECONDS : lVar != null && lVar.a(this);
    }

    @Override // j$.time.temporal.k
    public final long c(j$.time.temporal.l lVar) {
        if (lVar == j$.time.temporal.a.OFFSET_SECONDS) {
            return this.b;
        }
        if (lVar instanceof j$.time.temporal.a) {
            throw new p("Unsupported field: " + lVar);
        }
        return lVar.d(this);
    }

    @Override // java.lang.Comparable
    public final int compareTo(ZoneOffset zoneOffset) {
        return zoneOffset.b - this.b;
    }

    @Override // j$.time.temporal.k
    public final Object d(j$.time.temporal.n nVar) {
        return (nVar == j$.time.temporal.j.g() || nVar == j$.time.temporal.j.i()) ? this : j$.time.temporal.j.b(this, nVar);
    }

    @Override // j$.time.temporal.k
    public final int e(j$.time.temporal.a aVar) {
        if (aVar == j$.time.temporal.a.OFFSET_SECONDS) {
            return this.b;
        }
        if (aVar instanceof j$.time.temporal.a) {
            throw new p("Unsupported field: " + aVar);
        }
        return j$.time.temporal.j.c(this, aVar).a(c(aVar), aVar);
    }

    @Override // j$.time.ZoneId
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ZoneOffset) {
            return this.b == ((ZoneOffset) obj).b;
        }
        return false;
    }

    @Override // j$.time.ZoneId
    public final String getId() {
        return this.c;
    }

    @Override // j$.time.ZoneId
    public final ZoneRules getRules() {
        return ZoneRules.d(this);
    }

    public int getTotalSeconds() {
        return this.b;
    }

    @Override // j$.time.ZoneId
    public final int hashCode() {
        return this.b;
    }

    @Override // j$.time.ZoneId
    public final String toString() {
        return this.c;
    }
}
