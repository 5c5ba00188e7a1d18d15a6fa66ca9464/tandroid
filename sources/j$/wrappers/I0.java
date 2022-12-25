package j$.wrappers;

import j$.util.function.BiConsumer;
import j$.util.function.Function;
import java.util.Set;
import java.util.stream.Collector;
/* loaded from: classes2.dex */
public final /* synthetic */ class I0 {
    final /* synthetic */ Collector a;

    private /* synthetic */ I0(Collector collector) {
        this.a = collector;
    }

    public static /* synthetic */ I0 d(Collector collector) {
        if (collector == null) {
            return null;
        }
        return collector instanceof J0 ? ((J0) collector).a : new I0(collector);
    }

    public BiConsumer a() {
        return q.a(this.a.accumulator());
    }

    public Set b() {
        return this.a.characteristics();
    }

    public j$.util.function.b c() {
        return u.a(this.a.combiner());
    }

    public Function e() {
        return L.a(this.a.finisher());
    }

    public j$.util.function.y f() {
        return y0.a(this.a.supplier());
    }
}
