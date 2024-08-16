package j$.util.stream;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
/* loaded from: classes2.dex */
public final /* synthetic */ class j implements java.util.stream.Collector {
    public final /* synthetic */ Collector a;

    private /* synthetic */ j(Collector collector) {
        this.a = collector;
    }

    public static /* synthetic */ java.util.stream.Collector a(Collector collector) {
        if (collector == null) {
            return null;
        }
        return collector instanceof i ? ((i) collector).a : new j(collector);
    }

    @Override // java.util.stream.Collector
    public final /* synthetic */ BiConsumer accumulator() {
        return j$.util.function.a.a(this.a.accumulator());
    }

    @Override // java.util.stream.Collector
    public final /* synthetic */ Set characteristics() {
        return this.a.characteristics();
    }

    @Override // java.util.stream.Collector
    public final /* synthetic */ BinaryOperator combiner() {
        return j$.util.function.e.a(this.a.combiner());
    }

    public final /* synthetic */ boolean equals(Object obj) {
        Collector collector = this.a;
        if (obj instanceof j) {
            obj = ((j) obj).a;
        }
        return collector.equals(obj);
    }

    @Override // java.util.stream.Collector
    public final /* synthetic */ Function finisher() {
        return j$.util.function.y.a(this.a.finisher());
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // java.util.stream.Collector
    public final /* synthetic */ Supplier supplier() {
        return j$.util.function.u0.a(this.a.supplier());
    }
}
