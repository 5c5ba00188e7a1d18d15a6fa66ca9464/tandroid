package j$.util.stream;

import java.util.Arrays;

/* loaded from: classes2.dex */
final class u2 extends q2 {
    private J2 c;

    u2(e2 e2Var) {
        super(e2Var);
    }

    @Override // j$.util.stream.e2
    public final void accept(int i) {
        this.c.accept(i);
    }

    @Override // j$.util.stream.Y1, j$.util.stream.e2
    public final void m() {
        int[] iArr = (int[]) this.c.e();
        Arrays.sort(iArr);
        long length = iArr.length;
        e2 e2Var = this.a;
        e2Var.n(length);
        int i = 0;
        if (this.b) {
            int length2 = iArr.length;
            while (i < length2) {
                int i2 = iArr[i];
                if (e2Var.q()) {
                    break;
                }
                e2Var.accept(i2);
                i++;
            }
        } else {
            int length3 = iArr.length;
            while (i < length3) {
                e2Var.accept(iArr[i]);
                i++;
            }
        }
        e2Var.m();
    }

    @Override // j$.util.stream.Y1, j$.util.stream.e2
    public final void n(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.c = j > 0 ? new J2((int) j) : new J2();
    }
}
