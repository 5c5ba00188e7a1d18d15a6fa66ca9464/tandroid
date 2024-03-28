package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import j$.util.stream.Collector;
/* loaded from: classes2.dex */
class J2 extends V2 {
    final /* synthetic */ j$.util.function.b b;
    final /* synthetic */ BiConsumer c;
    final /* synthetic */ Supplier d;
    final /* synthetic */ Collector e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public J2(f4 f4Var, j$.util.function.b bVar, BiConsumer biConsumer, Supplier supplier, Collector collector) {
        super(f4Var);
        this.b = bVar;
        this.c = biConsumer;
        this.d = supplier;
        this.e = collector;
    }

    @Override // j$.util.stream.V2
    public T2 a() {
        return new K2(this.d, this.c, this.b);
    }

    @Override // j$.util.stream.V2, j$.util.stream.O4
    public int b() {
        if (this.e.characteristics().contains(Collector.a.UNORDERED)) {
            return e4.r;
        }
        return 0;
    }
}
