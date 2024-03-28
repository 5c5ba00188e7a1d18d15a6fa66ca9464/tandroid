package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.t;
/* loaded from: classes2.dex */
final class h2 extends j2 implements t.b {
    /* JADX INFO: Access modifiers changed from: package-private */
    public h2(x1 x1Var) {
        super(x1Var);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.k(this, consumer);
    }

    @Override // j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.c(this, consumer);
    }
}
