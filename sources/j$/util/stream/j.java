package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Function;
import j$.util.function.Supplier;
import java.util.Set;
/* loaded from: classes2.dex */
public final /* synthetic */ class j implements Collector {
    public final /* synthetic */ java.util.stream.Collector a;

    private /* synthetic */ j(java.util.stream.Collector collector) {
        this.a = collector;
    }

    public static /* synthetic */ Collector a(java.util.stream.Collector collector) {
        if (collector == null) {
            return null;
        }
        return collector instanceof k ? ((k) collector).a : new j(collector);
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
        if (obj instanceof j) {
            obj = ((j) obj).a;
        }
        return this.a.equals(obj);
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
        return j$.util.function.K0.a(this.a.supplier());
    }
}
