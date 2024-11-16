package j$.util.stream;

/* loaded from: classes2.dex */
final class W2 extends Y2 implements j$.util.function.F {
    final int[] c = new int[128];

    W2() {
    }

    @Override // j$.util.stream.Y2
    public final void a(Object obj, long j) {
        j$.util.function.F f = (j$.util.function.F) obj;
        for (int i = 0; i < j; i++) {
            f.accept(this.c[i]);
        }
    }

    @Override // j$.util.function.F
    public final void accept(int i) {
        int i2 = this.b;
        this.b = i2 + 1;
        this.c[i2] = i;
    }

    @Override // j$.util.function.F
    public final /* synthetic */ j$.util.function.F l(j$.util.function.F f) {
        return j$.com.android.tools.r8.a.c(this, f);
    }
}
