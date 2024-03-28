package j$.time.format;

import j$.time.ZoneOffset;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class j implements h {
    /* JADX INFO: Access modifiers changed from: package-private */
    public j(int i) {
    }

    @Override // j$.time.format.h
    public boolean a(t tVar, StringBuilder sb) {
        Long e = tVar.e(j$.time.temporal.a.INSTANT_SECONDS);
        j$.time.temporal.k d = tVar.d();
        j$.time.temporal.a aVar = j$.time.temporal.a.NANO_OF_SECOND;
        Long valueOf = d.e(aVar) ? Long.valueOf(tVar.d().c(aVar)) : null;
        int i = 0;
        if (e == null) {
            return false;
        }
        long longValue = e.longValue();
        int g = aVar.g(valueOf != null ? valueOf.longValue() : 0L);
        if (longValue >= -62167219200L) {
            long j = (longValue - 315569520000L) + 62167219200L;
            long d2 = j$.lang.d.d(j, 315569520000L) + 1;
            j$.time.e k = j$.time.e.k(j$.lang.d.c(j, 315569520000L) - 62167219200L, 0, ZoneOffset.f);
            if (d2 > 0) {
                sb.append('+');
                sb.append(d2);
            }
            sb.append(k);
            if (k.h() == 0) {
                sb.append(":00");
            }
        } else {
            long j2 = longValue + 62167219200L;
            long j3 = j2 / 315569520000L;
            long j4 = j2 % 315569520000L;
            j$.time.e k2 = j$.time.e.k(j4 - 62167219200L, 0, ZoneOffset.f);
            int length = sb.length();
            sb.append(k2);
            if (k2.h() == 0) {
                sb.append(":00");
            }
            if (j3 < 0) {
                if (k2.i() == -10000) {
                    sb.replace(length, length + 2, Long.toString(j3 - 1));
                } else if (j4 == 0) {
                    sb.insert(length, j3);
                } else {
                    sb.insert(length + 1, Math.abs(j3));
                }
            }
        }
        if (g > 0) {
            sb.append('.');
            int i2 = 100000000;
            while (true) {
                if (g <= 0 && i % 3 == 0 && i >= -2) {
                    break;
                }
                int i3 = g / i2;
                sb.append((char) (i3 + 48));
                g -= i3 * i2;
                i2 /= 10;
                i++;
            }
        }
        sb.append('Z');
        return true;
    }

    public String toString() {
        return "Instant()";
    }
}
