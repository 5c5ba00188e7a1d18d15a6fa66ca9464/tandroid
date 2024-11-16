package j$.util.stream;

/* loaded from: classes2.dex */
final class H extends K implements c2 {
    H() {
    }

    @Override // j$.util.stream.K, j$.util.stream.e2
    public final void accept(int i) {
        r(Integer.valueOf(i));
    }

    @Override // j$.util.function.Supplier
    public final Object get() {
        if (this.a) {
            return j$.util.m.d(((Integer) this.b).intValue());
        }
        return null;
    }

    @Override // j$.util.function.F
    public final /* synthetic */ j$.util.function.F l(j$.util.function.F f) {
        return j$.com.android.tools.r8.a.c(this, f);
    }
}
