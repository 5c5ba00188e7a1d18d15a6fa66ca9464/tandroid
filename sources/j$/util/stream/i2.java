package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.t;
/* loaded from: classes2.dex */
final class i2 extends j2 implements t.c {
    /* JADX INFO: Access modifiers changed from: package-private */
    public i2(z1 z1Var) {
        super(z1Var);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.l(this, consumer);
    }

    @Override // j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.d(this, consumer);
    }
}
