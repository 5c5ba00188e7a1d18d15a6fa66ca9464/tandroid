package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class I2 extends M2 implements j$.util.H {
    final /* synthetic */ J2 g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public I2(J2 j2, int i, int i2, int i3, int i4) {
        super(j2, i, i2, i3, i4);
        this.g = j2;
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        j$.util.a.f(this, consumer);
    }

    @Override // j$.util.stream.M2
    final void b(int i, Object obj, Object obj2) {
        ((j$.util.function.F) obj2).accept(((int[]) obj)[i]);
    }

    @Override // j$.util.stream.M2
    final j$.util.N f(Object obj, int i, int i2) {
        return j$.util.f0.k((int[]) obj, i, i2 + i);
    }

    @Override // j$.util.stream.M2
    final j$.util.N h(int i, int i2, int i3, int i4) {
        return new I2(this.g, i, i2, i3, i4);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return j$.util.a.o(this, consumer);
    }
}
