package j$.util.stream;

/* loaded from: classes2.dex */
final class X2 extends Y2 implements j$.util.function.W {
    final long[] c = new long[128];

    @Override // j$.util.stream.Y2
    public final void a(Object obj, long j) {
        j$.util.function.W w = (j$.util.function.W) obj;
        for (int i = 0; i < j; i++) {
            w.accept(this.c[i]);
        }
    }

    @Override // j$.util.function.W
    public final void accept(long j) {
        int i = this.b;
        this.b = i + 1;
        this.c[i] = j;
    }

    @Override // j$.util.function.W
    public final /* synthetic */ j$.util.function.W f(j$.util.function.W w) {
        return j$.com.android.tools.r8.a.d(this, w);
    }
}
