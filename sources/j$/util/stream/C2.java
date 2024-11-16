package j$.util.stream;

import java.util.Arrays;

/* loaded from: classes2.dex */
final class C2 extends q2 {
    private int[] c;
    private int d;

    C2(e2 e2Var) {
        super(e2Var);
    }

    @Override // j$.util.stream.e2
    public final void accept(int i) {
        int[] iArr = this.c;
        int i2 = this.d;
        this.d = i2 + 1;
        iArr[i2] = i;
    }

    @Override // j$.util.stream.Y1, j$.util.stream.e2
    public final void m() {
        int i = 0;
        Arrays.sort(this.c, 0, this.d);
        long j = this.d;
        e2 e2Var = this.a;
        e2Var.n(j);
        if (this.b) {
            while (i < this.d && !e2Var.q()) {
                e2Var.accept(this.c[i]);
                i++;
            }
        } else {
            while (i < this.d) {
                e2Var.accept(this.c[i]);
                i++;
            }
        }
        e2Var.m();
        this.c = null;
    }

    @Override // j$.util.stream.Y1, j$.util.stream.e2
    public final void n(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.c = new int[(int) j];
    }
}
