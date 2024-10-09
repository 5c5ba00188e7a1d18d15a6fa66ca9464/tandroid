package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Function;
import j$.util.function.Supplier;
import java.util.Set;

/* loaded from: classes2.dex */
public final /* synthetic */ class i implements Collector {
    public final /* synthetic */ java.util.stream.Collector a;

    private /* synthetic */ i(java.util.stream.Collector collector) {
        this.a = collector;
    }

    public static /* synthetic */ Collector a(java.util.stream.Collector collector) {
        if (collector == null) {
            return null;
        }
        return collector instanceof j ? ((j) collector).a : new i(collector);
    }

    @Override // j$.util.stream.Collector
    public final /* synthetic */ BiConsumer accumulator() {
        return BiConsumer.VivifiedWrapper.convert(this.a.accumulator());
    }

    @Override // j$.util.stream.Collector
    public final /* synthetic */ Set characteristics() {
        return this.a.characteristics();
    }

    @Override // j$.util.stream.Collector
    public final /* synthetic */ j$.util.function.f combiner() {
        return j$.util.function.d.a(this.a.combiner());
    }

    public final /* synthetic */ boolean equals(Object obj) {
        java.util.stream.Collector collector = this.a;
        if (obj instanceof i) {
            obj = ((i) obj).a;
        }
        return collector.equals(obj);
    }

    @Override // j$.util.stream.Collector
    public final /* synthetic */ Function finisher() {
        return Function.VivifiedWrapper.convert(this.a.finisher());
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.stream.Collector
    public final /* synthetic */ Supplier supplier() {
        return j$.util.function.t0.a(this.a.supplier());
    }
}
