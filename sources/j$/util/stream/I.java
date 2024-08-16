package j$.util.stream;
/* loaded from: classes2.dex */
final class I extends K implements d2 {
    @Override // j$.util.stream.K, j$.util.stream.e2
    public final void accept(long j) {
        r(Long.valueOf(j));
    }

    @Override // j$.util.function.W
    public final /* synthetic */ j$.util.function.W f(j$.util.function.W w) {
        return j$.com.android.tools.r8.a.d(this, w);
    }

    @Override // j$.util.function.Supplier
    public final Object get() {
        if (this.a) {
            return j$.util.n.d(((Long) this.b).longValue());
        }
        return null;
    }
}
