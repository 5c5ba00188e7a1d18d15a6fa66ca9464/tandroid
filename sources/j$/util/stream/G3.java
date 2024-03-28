package j$.util.stream;

import java.util.Comparator;
/* loaded from: classes2.dex */
abstract class G3 extends j3 {
    protected final Comparator b;
    protected boolean c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public G3(n3 n3Var, Comparator comparator) {
        super(n3Var);
        this.b = comparator;
    }

    @Override // j$.util.stream.j3, j$.util.stream.n3
    public final boolean o() {
        this.c = true;
        return false;
    }
}
