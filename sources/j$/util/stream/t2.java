package j$.util.stream;

import java.util.Arrays;

/* loaded from: classes2.dex */
final class t2 extends p2 {
    private H2 c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t2(e2 e2Var) {
        super(e2Var);
    }

    @Override // j$.util.stream.e2, j$.util.function.n
    public final void accept(double d) {
        this.c.accept(d);
    }

    @Override // j$.util.stream.X1, j$.util.stream.e2
    public final void m() {
        double[] dArr = (double[]) this.c.e();
        Arrays.sort(dArr);
        long length = dArr.length;
        e2 e2Var = this.a;
        e2Var.n(length);
        int i = 0;
        if (this.b) {
            int length2 = dArr.length;
            while (i < length2) {
                double d = dArr[i];
                if (e2Var.q()) {
                    break;
                }
                e2Var.accept(d);
                i++;
            }
        } else {
            int length3 = dArr.length;
            while (i < length3) {
                e2Var.accept(dArr[i]);
                i++;
            }
        }
        e2Var.m();
    }

    @Override // j$.util.stream.X1, j$.util.stream.e2
    public final void n(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.c = j > 0 ? new H2((int) j) : new H2();
    }
}
