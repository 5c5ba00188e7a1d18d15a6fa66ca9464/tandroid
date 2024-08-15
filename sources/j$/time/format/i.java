package j$.time.format;

import java.math.BigDecimal;
import java.math.RoundingMode;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class i implements h {
    private final j$.time.temporal.l a;
    private final int b;
    private final int c;
    private final boolean d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(j$.time.temporal.a aVar) {
        if (aVar == null) {
            throw new NullPointerException("field");
        }
        if (!aVar.c().f()) {
            throw new IllegalArgumentException("Field must have a fixed set of values: " + aVar);
        }
        this.a = aVar;
        this.b = 0;
        this.c = 9;
        this.d = true;
    }

    @Override // j$.time.format.h
    public final boolean a(t tVar, StringBuilder sb) {
        j$.time.temporal.l lVar = this.a;
        Long e = tVar.e(lVar);
        if (e == null) {
            return false;
        }
        w b = tVar.b();
        long longValue = e.longValue();
        j$.time.temporal.q c = lVar.c();
        c.b(longValue, lVar);
        BigDecimal valueOf = BigDecimal.valueOf(c.e());
        BigDecimal divide = BigDecimal.valueOf(longValue).subtract(valueOf).divide(BigDecimal.valueOf(c.d()).subtract(valueOf).add(BigDecimal.ONE), 9, RoundingMode.FLOOR);
        BigDecimal stripTrailingZeros = divide.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : divide.stripTrailingZeros();
        int scale = stripTrailingZeros.scale();
        boolean z = this.d;
        int i = this.b;
        if (scale != 0) {
            String substring = stripTrailingZeros.setScale(Math.min(Math.max(stripTrailingZeros.scale(), i), this.c), RoundingMode.FLOOR).toPlainString().substring(2);
            b.getClass();
            if (z) {
                sb.append('.');
            }
            sb.append(substring);
            return true;
        } else if (i > 0) {
            if (z) {
                b.getClass();
                sb.append('.');
            }
            for (int i2 = 0; i2 < i; i2++) {
                b.getClass();
                sb.append('0');
            }
            return true;
        } else {
            return true;
        }
    }

    public final String toString() {
        String str = this.d ? ",DecimalPoint" : "";
        return "Fraction(" + this.a + "," + this.b + "," + this.c + str + ")";
    }
}
