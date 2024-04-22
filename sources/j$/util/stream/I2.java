package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import j$.util.stream.Collector;
/* loaded from: classes2.dex */
class I2 extends U2 {
    final /* synthetic */ j$.util.function.b b;
    final /* synthetic */ BiConsumer c;
    final /* synthetic */ Supplier d;
    final /* synthetic */ Collector e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public I2(e4 e4Var, j$.util.function.b bVar, BiConsumer biConsumer, Supplier supplier, Collector collector) {
        super(e4Var);
        this.b = bVar;
        this.c = biConsumer;
        this.d = supplier;
        this.e = collector;
    }

    @Override // j$.util.stream.U2
    public S2 a() {
        return new J2(this.d, this.c, this.b);
    }

    @Override // j$.util.stream.U2, j$.util.stream.N4
    public int b() {
        if (this.e.characteristics().contains(Collector.a.UNORDERED)) {
            return d4.r;
        }
        return 0;
    }
}
