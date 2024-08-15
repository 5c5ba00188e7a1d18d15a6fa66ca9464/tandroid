package j$.util.function;
/* loaded from: classes2.dex */
public final /* synthetic */ class j implements m {
    public final /* synthetic */ m a;
    public final /* synthetic */ m b;

    public /* synthetic */ j(m mVar, m mVar2) {
        this.a = mVar;
        this.b = mVar2;
    }

    @Override // j$.util.function.m
    public final void accept(double d) {
        j$.time.a.b(this.a, this.b, d);
    }

    @Override // j$.util.function.m
    public final m m(m mVar) {
        mVar.getClass();
        return new j(this, mVar);
    }
}
