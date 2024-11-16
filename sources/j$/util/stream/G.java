package j$.util.stream;

/* loaded from: classes2.dex */
final class G extends K implements b2 {
    G() {
    }

    @Override // j$.util.stream.K, j$.util.stream.e2, j$.util.function.n
    public final void accept(double d) {
        r(Double.valueOf(d));
    }

    @Override // j$.util.function.Supplier
    public final Object get() {
        if (this.a) {
            return j$.util.l.d(((Double) this.b).doubleValue());
        }
        return null;
    }

    @Override // j$.util.function.n
    public final /* synthetic */ j$.util.function.n k(j$.util.function.n nVar) {
        return j$.com.android.tools.r8.a.b(this, nVar);
    }
}
