package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
/* loaded from: classes2.dex */
final class E1 extends u1 {
    final /* synthetic */ j$.util.function.f h;
    final /* synthetic */ BiConsumer i;
    final /* synthetic */ Supplier j;
    final /* synthetic */ Collector k;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public E1(U2 u2, j$.util.function.f fVar, BiConsumer biConsumer, Supplier supplier, Collector collector) {
        super(u2);
        this.h = fVar;
        this.i = biConsumer;
        this.j = supplier;
        this.k = collector;
    }

    @Override // j$.util.stream.u1, j$.util.stream.C3
    public final int b() {
        if (this.k.characteristics().contains(i.UNORDERED)) {
            return T2.r;
        }
        return 0;
    }

    @Override // j$.util.stream.u1
    public final O1 u() {
        return new F1(this.j, this.i, this.h);
    }
}
