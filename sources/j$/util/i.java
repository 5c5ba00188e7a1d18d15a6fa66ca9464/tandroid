package j$.util;

import org.telegram.tgnet.ConnectionsManager;

/* loaded from: classes2.dex */
public final class i implements j$.util.function.F {
    private long count;
    private long sum;
    private int min = ConnectionsManager.DEFAULT_DATACENTER_ID;
    private int max = Integer.MIN_VALUE;

    public final void a(i iVar) {
        this.count += iVar.count;
        this.sum += iVar.sum;
        this.min = Math.min(this.min, iVar.min);
        this.max = Math.max(this.max, iVar.max);
    }

    @Override // j$.util.function.F
    public final void accept(int i) {
        this.count++;
        this.sum += i;
        this.min = Math.min(this.min, i);
        this.max = Math.max(this.max, i);
    }

    @Override // j$.util.function.F
    public final /* synthetic */ j$.util.function.F l(j$.util.function.F f) {
        return j$.com.android.tools.r8.a.c(this, f);
    }

    public final String toString() {
        double d;
        String simpleName = i.class.getSimpleName();
        Long valueOf = Long.valueOf(this.count);
        Long valueOf2 = Long.valueOf(this.sum);
        Integer valueOf3 = Integer.valueOf(this.min);
        long j = this.count;
        if (j > 0) {
            double d2 = this.sum;
            double d3 = j;
            Double.isNaN(d2);
            Double.isNaN(d3);
            d = d2 / d3;
        } else {
            d = 0.0d;
        }
        return String.format("%s{count=%d, sum=%d, min=%d, average=%f, max=%d}", simpleName, valueOf, valueOf2, valueOf3, Double.valueOf(d), Integer.valueOf(this.max));
    }
}
