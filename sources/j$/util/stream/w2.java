package j$.util.stream;

import java.util.Arrays;
/* loaded from: classes2.dex */
final class w2 extends s2 {
    private M2 c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w2(f2 f2Var) {
        super(f2Var);
    }

    @Override // j$.util.stream.e2, j$.util.stream.f2
    public final void accept(long j) {
        this.c.accept(j);
    }

    @Override // j$.util.stream.a2, j$.util.stream.f2
    public final void end() {
        long[] jArr = (long[]) this.c.b();
        Arrays.sort(jArr);
        f2 f2Var = this.a;
        f2Var.f(jArr.length);
        int i = 0;
        if (this.b) {
            int length = jArr.length;
            while (i < length) {
                long j = jArr[i];
                if (f2Var.h()) {
                    break;
                }
                f2Var.accept(j);
                i++;
            }
        } else {
            int length2 = jArr.length;
            while (i < length2) {
                f2Var.accept(jArr[i]);
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
        this.c = j > 0 ? new M2((int) j) : new M2();
    }
}
