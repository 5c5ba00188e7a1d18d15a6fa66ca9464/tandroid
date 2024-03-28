package j$.wrappers;

import j$.util.function.BiConsumer;
import j$.util.function.Function;
import j$.util.function.Supplier;
import j$.util.stream.Collector;
import java.util.Set;
/* loaded from: classes2.dex */
public final /* synthetic */ class I0 implements Collector {
    final /* synthetic */ java.util.stream.Collector a;

    private /* synthetic */ I0(java.util.stream.Collector collector) {
        this.a = collector;
    }

    public static /* synthetic */ Collector a(java.util.stream.Collector collector) {
        if (collector == null) {
            return null;
        }
        return collector instanceof J0 ? ((J0) collector).a : new I0(collector);
    }

    @Override // j$.util.stream.Collector
    public /* synthetic */ BiConsumer accumulator() {
        return q.a(this.a.accumulator());
    }

    @Override // j$.util.stream.Collector
    public /* synthetic */ Set characteristics() {
        return this.a.characteristics();
    }

    @Override // j$.util.stream.Collector
    public /* synthetic */ j$.util.function.b combiner() {
        return u.a(this.a.combiner());
    }

    @Override // j$.util.stream.Collector
    public /* synthetic */ Function finisher() {
        return L.a(this.a.finisher());
    }

    @Override // j$.util.stream.Collector
    public /* synthetic */ Supplier supplier() {
        return y0.a(this.a.supplier());
    }
}
