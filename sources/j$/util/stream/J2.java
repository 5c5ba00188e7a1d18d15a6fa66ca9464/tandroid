package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class J2 extends N2 implements j$.util.H {
    final /* synthetic */ K2 g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public J2(K2 k2, int i, int i2, int i3, int i4) {
        super(k2, i, i2, i3, i4);
        this.g = k2;
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.p(this, consumer);
    }

    @Override // j$.util.stream.N2
    final void f(int i, Object obj, Object obj2) {
        ((j$.util.function.K) obj2).accept(((int[]) obj)[i]);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.g(this, consumer);
    }

    @Override // j$.util.stream.N2
    final j$.util.N g(Object obj, int i, int i2) {
        return j$.util.f0.k((int[]) obj, i, i2 + i);
    }

    @Override // j$.util.stream.N2
    final j$.util.N h(int i, int i2, int i3, int i4) {
        return new J2(this.g, i, i2, i3, i4);
    }
}
