package j$.time.format;

import j$.time.ZoneOffset;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class i implements g {
    @Override // j$.time.format.g
    public final boolean a(s sVar, StringBuilder sb) {
        Long e = sVar.e(j$.time.temporal.a.INSTANT_SECONDS);
        j$.time.temporal.k d = sVar.d();
        j$.time.temporal.a aVar = j$.time.temporal.a.NANO_OF_SECOND;
        Long valueOf = d.e(aVar) ? Long.valueOf(sVar.d().b(aVar)) : null;
        int i = 0;
        if (e == null) {
            return false;
        }
        long longValue = e.longValue();
        int f = aVar.f(valueOf != null ? valueOf.longValue() : 0L);
        if (longValue >= -62167219200L) {
            long j = longValue - 253402300800L;
            long j2 = j$.com.android.tools.r8.a.j(j, 315569520000L) + 1;
            j$.time.g j3 = j$.time.g.j(j$.com.android.tools.r8.a.i(j, 315569520000L) - 62167219200L, 0, ZoneOffset.f);
            if (j2 > 0) {
                sb.append('+');
                sb.append(j2);
            }
            sb.append(j3);
            if (j3.g() == 0) {
                sb.append(":00");
            }
        } else {
            long j4 = longValue + 62167219200L;
            long j5 = j4 / 315569520000L;
            long j6 = j4 % 315569520000L;
            j$.time.g j7 = j$.time.g.j(j6 - 62167219200L, 0, ZoneOffset.f);
            int length = sb.length();
            sb.append(j7);
            if (j7.g() == 0) {
                sb.append(":00");
            }
            if (j5 < 0) {
                if (j7.h() == -10000) {
                    sb.replace(length, length + 2, Long.toString(j5 - 1));
                } else if (j6 == 0) {
                    sb.insert(length, j5);
                } else {
                    sb.insert(length + 1, Math.abs(j5));
                }
            }
        }
        if (f > 0) {
            sb.append('.');
            int i2 = 100000000;
            while (true) {
                if (f <= 0 && i % 3 == 0 && i >= -2) {
                    break;
                }
                int i3 = f / i2;
                sb.append((char) (i3 + 48));
                f -= i3 * i2;
                i2 /= 10;
                i++;
            }
        }
        sb.append('Z');
        return true;
    }

    public final String toString() {
        return "Instant()";
    }
}
