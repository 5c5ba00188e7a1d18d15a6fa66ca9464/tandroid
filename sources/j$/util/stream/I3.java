package j$.util.stream;

import java.util.Arrays;
/* loaded from: classes2.dex */
final class I3 extends E3 {
    private X3 c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public I3(n3 n3Var) {
        super(n3Var);
    }

    @Override // j$.util.stream.l3, j$.util.stream.n3
    public void accept(int i) {
        this.c.accept(i);
    }

    @Override // j$.util.stream.h3, j$.util.stream.n3
    public void m() {
        int[] iArr = (int[]) this.c.e();
        Arrays.sort(iArr);
        this.a.n(iArr.length);
        int i = 0;
        if (this.b) {
            int length = iArr.length;
            while (i < length) {
                int i2 = iArr[i];
                if (this.a.o()) {
                    break;
                }
                this.a.accept(i2);
                i++;
            }
        } else {
            int length2 = iArr.length;
            while (i < length2) {
                this.a.accept(iArr[i]);
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
        this.c = j > 0 ? new X3((int) j) : new X3();
    }
}
