package j$.time.format;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class i implements h {
    private final j$.time.temporal.l a;
    private final int b;
    private final int c;
    private final boolean d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(j$.time.temporal.l lVar, int i, int i2, boolean z) {
        Objects.requireNonNull(lVar, "field");
        if (!lVar.a().f()) {
            throw new IllegalArgumentException("Field must have a fixed set of values: " + lVar);
        } else if (i < 0 || i > 9) {
            throw new IllegalArgumentException("Minimum width must be from 0 to 9 inclusive but was " + i);
        } else if (i2 < 1 || i2 > 9) {
            throw new IllegalArgumentException("Maximum width must be from 1 to 9 inclusive but was " + i2);
        } else if (i2 >= i) {
            this.a = lVar;
            this.b = i;
            this.c = i2;
            this.d = z;
        } else {
            throw new IllegalArgumentException("Maximum width must exceed or equal the minimum width but " + i2 + " < " + i);
        }
    }

    @Override // j$.time.format.h
    public boolean a(t tVar, StringBuilder sb) {
        Long e = tVar.e(this.a);
        if (e == null) {
            return false;
        }
        w b = tVar.b();
        long longValue = e.longValue();
        j$.time.temporal.x a = this.a.a();
        a.b(longValue, this.a);
        BigDecimal valueOf = BigDecimal.valueOf(a.e());
        BigDecimal divide = BigDecimal.valueOf(longValue).subtract(valueOf).divide(BigDecimal.valueOf(a.d()).subtract(valueOf).add(BigDecimal.ONE), 9, RoundingMode.FLOOR);
        BigDecimal stripTrailingZeros = divide.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : divide.stripTrailingZeros();
        if (stripTrailingZeros.scale() != 0) {
            String a2 = b.a(stripTrailingZeros.setScale(Math.min(Math.max(stripTrailingZeros.scale(), this.b), this.c), RoundingMode.FLOOR).toPlainString().substring(2));
            if (this.d) {
                sb.append(b.b());
            }
            sb.append(a2);
            return true;
        } else if (this.b > 0) {
            if (this.d) {
                sb.append(b.b());
            }
            for (int i = 0; i < this.b; i++) {
                sb.append(b.e());
            }
            return true;
        } else {
            return true;
        }
    }

    public String toString() {
        String str = this.d ? ",DecimalPoint" : "";
        return "Fraction(" + this.a + "," + this.b + "," + this.c + str + ")";
    }
}
