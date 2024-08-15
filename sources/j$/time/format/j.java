package j$.time.format;

import j$.time.ZoneOffset;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class j implements h {
    @Override // j$.time.format.h
    public final boolean a(t tVar, StringBuilder sb) {
        Long e = tVar.e(j$.time.temporal.a.INSTANT_SECONDS);
        j$.time.temporal.k d = tVar.d();
        j$.time.temporal.a aVar = j$.time.temporal.a.NANO_OF_SECOND;
        Long valueOf = d.b(aVar) ? Long.valueOf(tVar.d().c(aVar)) : null;
        int i = 0;
        if (e == null) {
            return false;
        }
        long longValue = e.longValue();
        int e2 = aVar.e(valueOf != null ? valueOf.longValue() : 0L);
        if (longValue >= -62167219200L) {
            long j = (longValue - 315569520000L) + 62167219200L;
            long f = j$.time.a.f(j, 315569520000L) + 1;
            j$.time.h j2 = j$.time.h.j(j$.time.a.d(j, 315569520000L) - 62167219200L, 0, ZoneOffset.f);
            if (f > 0) {
                sb.append('+');
                sb.append(f);
            }
            sb.append(j2);
            if (j2.g() == 0) {
                sb.append(":00");
            }
        } else {
            long j3 = longValue + 62167219200L;
            long j4 = j3 / 315569520000L;
            long j5 = j3 % 315569520000L;
            j$.time.h j6 = j$.time.h.j(j5 - 62167219200L, 0, ZoneOffset.f);
            int length = sb.length();
            sb.append(j6);
            if (j6.g() == 0) {
                sb.append(":00");
            }
            if (j4 < 0) {
                if (j6.h() == -10000) {
                    sb.replace(length, length + 2, Long.toString(j4 - 1));
                } else if (j5 == 0) {
                    sb.insert(length, j4);
                } else {
                    sb.insert(length + 1, Math.abs(j4));
                }
            }
        }
        if (e2 > 0) {
            sb.append('.');
            int i2 = 100000000;
            while (true) {
                if (e2 <= 0 && i % 3 == 0 && i >= -2) {
                    break;
                }
                int i3 = e2 / i2;
                sb.append((char) (i3 + 48));
                e2 -= i3 * i2;
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
