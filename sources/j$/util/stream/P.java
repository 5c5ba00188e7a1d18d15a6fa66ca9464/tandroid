package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class P extends S implements e2 {
    final j$.util.function.h0 b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public P(j$.util.function.h0 h0Var, boolean z) {
        super(z);
        this.b = h0Var;
    }

    @Override // j$.util.stream.S, j$.util.stream.f2
    public final void accept(long j) {
        this.b.accept(j);
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        l((Long) obj);
    }

    @Override // j$.util.function.h0
    public final j$.util.function.h0 i(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        return new j$.util.function.e0(this, h0Var);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void l(Long l) {
        u0.n0(this, l);
    }
}
