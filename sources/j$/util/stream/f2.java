package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class f2 extends i2 implements j$.util.t {
    /* JADX INFO: Access modifiers changed from: package-private */
    public f2(u1 u1Var) {
        super(u1Var);
    }

    @Override // j$.util.u
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.j(this, consumer);
    }

    @Override // j$.util.u
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.b(this, consumer);
    }
}
