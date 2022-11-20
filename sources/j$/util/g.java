package j$.util;

import java.util.Objects;
/* loaded from: classes2.dex */
public class g implements j$.util.function.f {
    private double a;
    private double b;
    private long count;
    private double sum;
    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;

    private void d(double d) {
        double d2 = d - this.a;
        double d3 = this.sum;
        double d4 = d3 + d2;
        this.a = (d4 - d3) - d2;
        this.sum = d4;
    }

    @Override // j$.util.function.f
    public void accept(double d) {
        this.count++;
        this.b += d;
        d(d);
        this.min = Math.min(this.min, d);
        this.max = Math.max(this.max, d);
    }

    public void b(g gVar) {
        this.count += gVar.count;
        this.b += gVar.b;
        d(gVar.sum);
        d(gVar.a);
        this.min = Math.min(this.min, gVar.min);
        this.max = Math.max(this.max, gVar.max);
    }

    public final double c() {
        double d = this.sum + this.a;
        return (!Double.isNaN(d) || !Double.isInfinite(this.b)) ? d : this.b;
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return new j$.util.function.e(this, fVar);
    }

    public String toString() {
        double d;
        Object[] objArr = new Object[6];
        objArr[0] = g.class.getSimpleName();
        objArr[1] = Long.valueOf(this.count);
        objArr[2] = Double.valueOf(c());
        objArr[3] = Double.valueOf(this.min);
        if (this.count > 0) {
            double c = c();
            double d2 = this.count;
            Double.isNaN(d2);
            Double.isNaN(d2);
            d = c / d2;
        } else {
            d = 0.0d;
        }
        objArr[4] = Double.valueOf(d);
        objArr[5] = Double.valueOf(this.max);
        return String.format("%s{count=%d, sum=%f, min=%f, average=%f, max=%f}", objArr);
    }
}
