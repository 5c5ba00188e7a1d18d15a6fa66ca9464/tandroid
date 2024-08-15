package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class c1 extends f1 implements j$.util.E {
    /* JADX INFO: Access modifiers changed from: package-private */
    public c1(z0 z0Var) {
        super(z0Var);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.n(this, consumer);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.f(this, consumer);
    }
}
