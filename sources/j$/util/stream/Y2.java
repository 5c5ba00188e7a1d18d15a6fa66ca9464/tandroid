package j$.util.stream;
/* loaded from: classes2.dex */
final class Y2 extends Z2 implements j$.util.function.h0 {
    final long[] c = new long[128];

    @Override // j$.util.stream.Z2
    public final void a(Object obj, long j) {
        j$.util.function.h0 h0Var = (j$.util.function.h0) obj;
        for (int i = 0; i < j; i++) {
            h0Var.accept(this.c[i]);
        }
    }

    @Override // j$.util.function.h0
    public final void accept(long j) {
        int i = this.b;
        this.b = i + 1;
        this.c[i] = j;
    }

    @Override // j$.util.function.h0
    public final j$.util.function.h0 i(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        return new j$.util.function.e0(this, h0Var);
    }
}
