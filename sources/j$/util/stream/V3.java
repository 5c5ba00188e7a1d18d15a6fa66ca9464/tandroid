package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.s;
import j$.util.stream.Z3;
/* loaded from: classes2.dex */
class V3 extends Z3.a implements s.b {
    final /* synthetic */ W3 g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public V3(W3 w3, int i, int i2, int i3, int i4) {
        super(i, i2, i3, i4);
        this.g = w3;
    }

    @Override // j$.util.stream.Z3.a
    void a(Object obj, int i, Object obj2) {
        ((j$.util.function.l) obj2).accept(((int[]) obj)[i]);
    }

    @Override // j$.util.s
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.k(this, consumer);
    }

    @Override // j$.util.stream.Z3.a
    j$.util.t f(Object obj, int i, int i2) {
        return j$.util.I.k((int[]) obj, i, i2 + i, 1040);
    }

    @Override // j$.util.s
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.c(this, consumer);
    }

    @Override // j$.util.stream.Z3.a
    j$.util.t h(int i, int i2, int i3, int i4) {
        return new V3(this.g, i, i2, i3, i4);
    }
}
