package j$.time.format;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class h implements g {
    private final j$.time.temporal.l a;
    private final int b;
    private final int c;
    private final boolean d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(j$.time.temporal.a aVar) {
        j$.util.a.B(aVar, "field");
        if (!aVar.a().f()) {
            throw new IllegalArgumentException("Field must have a fixed set of values: " + aVar);
        }
        this.a = aVar;
        this.b = 0;
        this.c = 9;
        this.d = true;
    }

    @Override // j$.time.format.g
    public final boolean a(s sVar, StringBuilder sb) {
        j$.time.temporal.l lVar = this.a;
        Long e = sVar.e(lVar);
        if (e == null) {
            return false;
        }
        w b = sVar.b();
        long longValue = e.longValue();
        j$.time.temporal.q a = lVar.a();
        a.b(longValue, lVar);
        BigDecimal valueOf = BigDecimal.valueOf(a.e());
        BigDecimal add = BigDecimal.valueOf(a.d()).subtract(valueOf).add(BigDecimal.ONE);
        BigDecimal subtract = BigDecimal.valueOf(longValue).subtract(valueOf);
        RoundingMode roundingMode = RoundingMode.FLOOR;
        BigDecimal divide = subtract.divide(add, 9, roundingMode);
        BigDecimal bigDecimal = BigDecimal.ZERO;
        if (divide.compareTo(bigDecimal) != 0) {
            bigDecimal = divide.signum() == 0 ? new BigDecimal(BigInteger.ZERO, 0) : divide.stripTrailingZeros();
        }
        int scale = bigDecimal.scale();
        boolean z = this.d;
        int i = this.b;
        if (scale != 0) {
            String substring = bigDecimal.setScale(Math.min(Math.max(bigDecimal.scale(), i), this.c), roundingMode).toPlainString().substring(2);
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
