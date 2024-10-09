package j$.util.stream;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class M extends Q implements b2 {
    final j$.util.function.n b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public M(j$.util.function.n nVar, boolean z) {
        super(z);
        this.b = nVar;
    }

    @Override // j$.util.stream.Q, j$.util.stream.e2, j$.util.function.n
    public final void accept(double d) {
        this.b.accept(d);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public final /* bridge */ /* synthetic */ void r(Object obj) {
        r((Double) obj);
    }

    @Override // j$.util.function.n
    public final /* synthetic */ j$.util.function.n k(j$.util.function.n nVar) {
        return j$.com.android.tools.r8.a.b(this, nVar);
    }

    @Override // j$.util.stream.b2
    public final /* synthetic */ void r(Double d) {
        t0.e(this, d);
    }
}
