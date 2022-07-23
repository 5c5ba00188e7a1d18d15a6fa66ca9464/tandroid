package j$.util.stream;

import java.util.Comparator;
/* loaded from: classes2.dex */
abstract class F3 extends i3 {
    protected final Comparator b;
    protected boolean c;

    public F3(m3 m3Var, Comparator comparator) {
        super(m3Var);
        this.b = comparator;
    }

    @Override // j$.util.stream.i3, j$.util.stream.m3
    public final boolean o() {
        this.c = true;
        return false;
    }
}
