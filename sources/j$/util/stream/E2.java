package j$.util.stream;

import java.util.Arrays;
import java.util.Comparator;

/* loaded from: classes2.dex */
final class E2 extends s2 {
    private Object[] d;
    private int e;

    E2(e2 e2Var, Comparator comparator) {
        super(e2Var, comparator);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public final void r(Object obj) {
        Object[] objArr = this.d;
        int i = this.e;
        this.e = i + 1;
        objArr[i] = obj;
    }

    @Override // j$.util.stream.a2, j$.util.stream.e2
    public final void m() {
        int i = 0;
        Arrays.sort(this.d, 0, this.e, this.b);
        long j = this.e;
        e2 e2Var = this.a;
        e2Var.n(j);
        if (this.c) {
            while (i < this.e && !e2Var.q()) {
                e2Var.r((e2) this.d[i]);
                i++;
            }
        } else {
            while (i < this.e) {
                e2Var.r((e2) this.d[i]);
                i++;
            }
        }
        e2Var.m();
        this.d = null;
    }

    @Override // j$.util.stream.a2, j$.util.stream.e2
    public final void n(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.d = new Object[(int) j];
    }
}
