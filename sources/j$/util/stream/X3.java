package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.stream.Z3;
import j$.util.t;
/* loaded from: classes2.dex */
class X3 extends Z3.a implements t.c {
    final /* synthetic */ Y3 g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public X3(Y3 y3, int i, int i2, int i3, int i4) {
        super(i, i2, i3, i4);
        this.g = y3;
    }

    @Override // j$.util.stream.Z3.a
    void a(Object obj, int i, Object obj2) {
        ((j$.util.function.q) obj2).accept(((long[]) obj)[i]);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.l(this, consumer);
    }

    @Override // j$.util.stream.Z3.a
    j$.util.u f(Object obj, int i, int i2) {
        return j$.util.J.l((long[]) obj, i, i2 + i, 1040);
    }

    @Override // j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.d(this, consumer);
    }

    @Override // j$.util.stream.Z3.a
    j$.util.u h(int i, int i2, int i3, int i4) {
        return new X3(this.g, i, i2, i3, i4);
    }
}
