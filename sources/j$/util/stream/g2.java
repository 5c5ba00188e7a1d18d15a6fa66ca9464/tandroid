package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.t;
/* loaded from: classes2.dex */
final class g2 extends j2 implements t.a {
    /* JADX INFO: Access modifiers changed from: package-private */
    public g2(v1 v1Var) {
        super(v1Var);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.j(this, consumer);
    }

    @Override // j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.b(this, consumer);
    }
}
