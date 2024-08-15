package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class L2 extends N2 implements j$.util.K {
    final /* synthetic */ M2 g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public L2(M2 m2, int i, int i2, int i3, int i4) {
        super(m2, i, i2, i3, i4);
        this.g = m2;
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.q(this, consumer);
    }

    @Override // j$.util.stream.N2
    final void f(int i, Object obj, Object obj2) {
        ((j$.util.function.h0) obj2).accept(((long[]) obj)[i]);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.h(this, consumer);
    }

    @Override // j$.util.stream.N2
    final j$.util.N g(Object obj, int i, int i2) {
        return j$.util.f0.l((long[]) obj, i, i2 + i);
    }

    @Override // j$.util.stream.N2
    final j$.util.N h(int i, int i2, int i3, int i4) {
        return new L2(this.g, i, i2, i3, i4);
    }
}
