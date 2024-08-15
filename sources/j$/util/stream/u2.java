package j$.util.stream;

import java.util.Arrays;
/* loaded from: classes2.dex */
final class u2 extends q2 {
    private I2 c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public u2(f2 f2Var) {
        super(f2Var);
    }

    @Override // j$.util.stream.c2, j$.util.function.m
    public final void accept(double d) {
        this.c.accept(d);
    }

    @Override // j$.util.stream.Y1, j$.util.stream.f2
    public final void end() {
        double[] dArr = (double[]) this.c.b();
        Arrays.sort(dArr);
        f2 f2Var = this.a;
        f2Var.f(dArr.length);
        int i = 0;
        if (this.b) {
            int length = dArr.length;
            while (i < length) {
                double d = dArr[i];
                if (f2Var.h()) {
                    break;
                }
                f2Var.accept(d);
                i++;
            }
        } else {
            int length2 = dArr.length;
            while (i < length2) {
                f2Var.accept(dArr[i]);
                i++;
            }
        }
        f2Var.end();
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.c = j > 0 ? new I2((int) j) : new I2();
    }
}
