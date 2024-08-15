package j$.util.stream;

import java.util.Arrays;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class F2 extends t2 {
    private Object[] d;
    private int e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public F2(f2 f2Var, Comparator comparator) {
        super(f2Var, comparator);
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        Object[] objArr = this.d;
        int i = this.e;
        this.e = i + 1;
        objArr[i] = obj;
    }

    @Override // j$.util.stream.b2, j$.util.stream.f2
    public final void end() {
        int i = 0;
        Arrays.sort(this.d, 0, this.e, this.b);
        f2 f2Var = this.a;
        f2Var.f(this.e);
        if (this.c) {
            while (i < this.e && !f2Var.h()) {
                f2Var.accept((f2) this.d[i]);
                i++;
            }
        } else {
            while (i < this.e) {
                f2Var.accept((f2) this.d[i]);
                i++;
            }
        }
        f2Var.end();
        this.d = null;
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.d = new Object[(int) j];
    }
}
