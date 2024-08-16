package j$.util;
/* loaded from: classes2.dex */
public final class h implements j$.util.function.n {
    private double a;
    private double b;
    private long count;
    private double sum;
    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;

    private void c(double d) {
        double d2 = d - this.a;
        double d3 = this.sum;
        double d4 = d3 + d2;
        this.a = (d4 - d3) - d2;
        this.sum = d4;
    }

    public final void a(h hVar) {
        this.count += hVar.count;
        this.b += hVar.b;
        c(hVar.sum);
        c(hVar.a);
        this.min = Math.min(this.min, hVar.min);
        this.max = Math.max(this.max, hVar.max);
    }

    @Override // j$.util.function.n
    public final void accept(double d) {
        this.count++;
        this.b += d;
        c(d);
        this.min = Math.min(this.min, d);
        this.max = Math.max(this.max, d);
    }

    @Override // j$.util.function.n
    public final /* synthetic */ j$.util.function.n k(j$.util.function.n nVar) {
        return j$.com.android.tools.r8.a.b(this, nVar);
    }

    public final String toString() {
        double d;
        String simpleName = h.class.getSimpleName();
        Long valueOf = Long.valueOf(this.count);
        double d2 = this.sum + this.a;
        if (Double.isNaN(d2) && Double.isInfinite(this.b)) {
            d2 = this.b;
        }
        Double valueOf2 = Double.valueOf(d2);
        Double valueOf3 = Double.valueOf(this.min);
        if (this.count > 0) {
            double d3 = this.sum + this.a;
            if (Double.isNaN(d3) && Double.isInfinite(this.b)) {
                d3 = this.b;
            }
            double d4 = this.count;
            Double.isNaN(d4);
            d = d3 / d4;
        } else {
            d = 0.0d;
        }
        return String.format("%s{count=%d, sum=%f, min=%f, average=%f, max=%f}", simpleName, valueOf, valueOf2, valueOf3, Double.valueOf(d), Double.valueOf(this.max));
    }
}
