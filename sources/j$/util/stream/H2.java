package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class H2 extends N2 implements j$.util.E {
    final /* synthetic */ I2 g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public H2(I2 i2, int i, int i3, int i4, int i5) {
        super(i2, i, i3, i4, i5);
        this.g = i2;
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.n(this, consumer);
    }

    @Override // j$.util.stream.N2
    final void f(int i, Object obj, Object obj2) {
        ((j$.util.function.m) obj2).accept(((double[]) obj)[i]);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.f(this, consumer);
    }

    @Override // j$.util.stream.N2
    final j$.util.N g(Object obj, int i, int i2) {
        return j$.util.f0.j((double[]) obj, i, i2 + i);
    }

    @Override // j$.util.stream.N2
    final j$.util.N h(int i, int i2, int i3, int i4) {
        return new H2(this.g, i, i2, i3, i4);
    }
}
