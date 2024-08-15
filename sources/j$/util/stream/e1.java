package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class e1 extends f1 implements j$.util.K {
    /* JADX INFO: Access modifiers changed from: package-private */
    public e1(B0 b0) {
        super(b0);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.q(this, consumer);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.h(this, consumer);
    }
}
