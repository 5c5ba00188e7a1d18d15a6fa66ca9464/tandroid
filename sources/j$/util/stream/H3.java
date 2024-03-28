package j$.util.stream;

import java.util.Arrays;
/* loaded from: classes2.dex */
final class H3 extends D3 {
    private V3 c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public H3(n3 n3Var) {
        super(n3Var);
    }

    @Override // j$.util.stream.k3, j$.util.stream.n3
    public void accept(double d) {
        this.c.accept(d);
    }

    @Override // j$.util.stream.g3, j$.util.stream.n3
    public void m() {
        double[] dArr = (double[]) this.c.e();
        Arrays.sort(dArr);
        this.a.n(dArr.length);
        int i = 0;
        if (this.b) {
            int length = dArr.length;
            while (i < length) {
                double d = dArr[i];
                if (this.a.o()) {
                    break;
                }
                this.a.accept(d);
                i++;
            }
        } else {
            int length2 = dArr.length;
            while (i < length2) {
                this.a.accept(dArr[i]);
                i++;
            }
        }
        this.a.m();
    }

    @Override // j$.util.stream.n3
    public void n(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.c = j > 0 ? new V3((int) j) : new V3();
    }
}
