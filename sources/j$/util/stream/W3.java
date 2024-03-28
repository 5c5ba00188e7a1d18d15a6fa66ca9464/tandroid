package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.stream.a4;
import j$.util.t;
/* loaded from: classes2.dex */
class W3 extends a4.a implements t.b {
    final /* synthetic */ X3 g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public W3(X3 x3, int i, int i2, int i3, int i4) {
        super(i, i2, i3, i4);
        this.g = x3;
    }

    @Override // j$.util.stream.a4.a
    void a(Object obj, int i, Object obj2) {
        ((j$.util.function.l) obj2).accept(((int[]) obj)[i]);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.k(this, consumer);
    }

    @Override // j$.util.stream.a4.a
    j$.util.u f(Object obj, int i, int i2) {
        return j$.util.J.k((int[]) obj, i, i2 + i, 1040);
    }

    @Override // j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.c(this, consumer);
    }

    @Override // j$.util.stream.a4.a
    j$.util.u h(int i, int i2, int i3, int i4) {
        return new W3(this.g, i, i2, i3, i4);
    }
}
