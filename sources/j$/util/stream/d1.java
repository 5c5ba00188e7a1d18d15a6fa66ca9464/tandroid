package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class d1 extends f1 implements j$.util.H {
    /* JADX INFO: Access modifiers changed from: package-private */
    public d1(A0 a0) {
        super(a0);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.p(this, consumer);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.g(this, consumer);
    }
}
