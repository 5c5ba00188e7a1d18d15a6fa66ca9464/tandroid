package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class N extends S implements c2 {
    final j$.util.function.m b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public N(j$.util.function.m mVar, boolean z) {
        super(z);
        this.b = mVar;
    }

    @Override // j$.util.stream.S, j$.util.stream.f2, j$.util.stream.c2, j$.util.function.m
    public final void accept(double d) {
        this.b.accept(d);
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        p((Double) obj);
    }

    @Override // j$.util.function.m
    public final j$.util.function.m m(j$.util.function.m mVar) {
        mVar.getClass();
        return new j$.util.function.j(this, mVar);
    }

    @Override // j$.util.stream.c2
    public final /* synthetic */ void p(Double d) {
        u0.j0(this, d);
    }
}
