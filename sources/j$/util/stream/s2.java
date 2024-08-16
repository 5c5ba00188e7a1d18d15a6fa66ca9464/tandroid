package j$.util.stream;

import java.util.Comparator;
/* loaded from: classes2.dex */
abstract class s2 extends a2 {
    protected final Comparator b;
    protected boolean c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s2(e2 e2Var, Comparator comparator) {
        super(e2Var);
        this.b = comparator;
    }

    @Override // j$.util.stream.a2, j$.util.stream.e2
    public final boolean q() {
        this.c = true;
        return false;
    }
}
