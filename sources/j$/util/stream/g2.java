package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.s;
/* loaded from: classes2.dex */
final class g2 extends i2 implements s.b {
    /* JADX INFO: Access modifiers changed from: package-private */
    public g2(w1 w1Var) {
        super(w1Var);
    }

    @Override // j$.util.s
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.k(this, consumer);
    }

    @Override // j$.util.s
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.c(this, consumer);
    }
}
