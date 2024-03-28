package j$.util.stream;

import java.util.Arrays;
/* loaded from: classes2.dex */
final class J3 extends F3 {
    private Z3 c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public J3(n3 n3Var) {
        super(n3Var);
    }

    @Override // j$.util.stream.m3, j$.util.function.q
    public void accept(long j) {
        this.c.accept(j);
    }

    @Override // j$.util.stream.i3, j$.util.stream.n3
    public void m() {
        long[] jArr = (long[]) this.c.e();
        Arrays.sort(jArr);
        this.a.n(jArr.length);
        int i = 0;
        if (this.b) {
            int length = jArr.length;
            while (i < length) {
                long j = jArr[i];
                if (this.a.o()) {
                    break;
                }
                this.a.accept(j);
                i++;
            }
        } else {
            int length2 = jArr.length;
            while (i < length2) {
                this.a.accept(jArr[i]);
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
        this.c = j > 0 ? new Z3((int) j) : new Z3();
    }
}
