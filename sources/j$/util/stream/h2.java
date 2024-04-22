package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.s;
/* loaded from: classes2.dex */
final class h2 extends i2 implements s.c {
    /* JADX INFO: Access modifiers changed from: package-private */
    public h2(y1 y1Var) {
        super(y1Var);
    }

    @Override // j$.util.s
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.l(this, consumer);
    }

    @Override // j$.util.s
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.d(this, consumer);
    }
}
