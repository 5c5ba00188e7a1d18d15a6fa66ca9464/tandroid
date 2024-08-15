package j$.util.stream;

import java.util.Comparator;
/* loaded from: classes2.dex */
abstract class t2 extends b2 {
    protected final Comparator b;
    protected boolean c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t2(f2 f2Var, Comparator comparator) {
        super(f2Var);
        this.b = comparator;
    }

    @Override // j$.util.stream.b2, j$.util.stream.f2
    public final boolean h() {
        this.c = true;
        return false;
    }
}
