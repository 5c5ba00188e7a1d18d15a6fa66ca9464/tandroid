package j$.util.stream;
/* loaded from: classes2.dex */
final class m1 extends d {
    private final l1 j;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m1(l1 l1Var, y2 y2Var, j$.util.t tVar) {
        super(y2Var, tVar);
        this.j = l1Var;
    }

    m1(m1 m1Var, j$.util.t tVar) {
        super(m1Var, tVar);
        this.j = m1Var.j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public Object a() {
        boolean z;
        y2 y2Var = this.a;
        j1 j1Var = (j1) this.j.c.get();
        y2Var.u0(j1Var, this.b);
        boolean z2 = j1Var.b;
        z = this.j.b.b;
        if (z2 == z) {
            l(Boolean.valueOf(z2));
            return null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public f f(j$.util.t tVar) {
        return new m1(this, tVar);
    }

    @Override // j$.util.stream.d
    protected Object k() {
        boolean z;
        z = this.j.b.b;
        return Boolean.valueOf(!z);
    }
}
