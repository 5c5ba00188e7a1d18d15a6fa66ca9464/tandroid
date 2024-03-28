package j$.util.stream;
/* loaded from: classes2.dex */
final class n1 extends d {
    private final m1 j;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n1(m1 m1Var, z2 z2Var, j$.util.t tVar) {
        super(z2Var, tVar);
        this.j = m1Var;
    }

    n1(n1 n1Var, j$.util.t tVar) {
        super(n1Var, tVar);
        this.j = n1Var.j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public Object a() {
        boolean z;
        z2 z2Var = this.a;
        k1 k1Var = (k1) this.j.c.get();
        z2Var.s0(k1Var, this.b);
        boolean z2 = k1Var.b;
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
        return new n1(this, tVar);
    }

    @Override // j$.util.stream.d
    protected Object k() {
        boolean z;
        z = this.j.b.b;
        return Boolean.valueOf(!z);
    }
}
