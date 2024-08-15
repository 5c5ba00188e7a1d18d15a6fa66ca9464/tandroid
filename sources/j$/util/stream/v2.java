package j$.util.stream;

import java.util.Arrays;
/* loaded from: classes2.dex */
final class v2 extends r2 {
    private K2 c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public v2(f2 f2Var) {
        super(f2Var);
    }

    @Override // j$.util.stream.d2, j$.util.stream.f2
    public final void accept(int i) {
        this.c.accept(i);
    }

    @Override // j$.util.stream.Z1, j$.util.stream.f2
    public final void end() {
        int[] iArr = (int[]) this.c.b();
        Arrays.sort(iArr);
        f2 f2Var = this.a;
        f2Var.f(iArr.length);
        int i = 0;
        if (this.b) {
            int length = iArr.length;
            while (i < length) {
                int i2 = iArr[i];
                if (f2Var.h()) {
                    break;
                }
                f2Var.accept(i2);
                i++;
            }
        } else {
            int length2 = iArr.length;
            while (i < length2) {
                f2Var.accept(iArr[i]);
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
        this.c = j > 0 ? new K2((int) j) : new K2();
    }
}
