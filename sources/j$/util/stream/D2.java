package j$.util.stream;

import java.util.Arrays;
/* loaded from: classes2.dex */
final class D2 extends r2 {
    private int[] c;
    private int d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public D2(f2 f2Var) {
        super(f2Var);
    }

    @Override // j$.util.stream.d2, j$.util.stream.f2
    public final void accept(int i) {
        int[] iArr = this.c;
        int i2 = this.d;
        this.d = i2 + 1;
        iArr[i2] = i;
    }

    @Override // j$.util.stream.Z1, j$.util.stream.f2
    public final void end() {
        int i = 0;
        Arrays.sort(this.c, 0, this.d);
        f2 f2Var = this.a;
        f2Var.f(this.d);
        if (this.b) {
            while (i < this.d && !f2Var.h()) {
                f2Var.accept(this.c[i]);
                i++;
            }
        } else {
            while (i < this.d) {
                f2Var.accept(this.c[i]);
                i++;
            }
        }
        f2Var.end();
        this.c = null;
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.c = new int[(int) j];
    }
}
