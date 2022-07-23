package j$.util.stream;
/* loaded from: classes2.dex */
final class m1 extends d {
    private final l1 j;

    public m1(l1 l1Var, y2 y2Var, j$.util.u uVar) {
        super(y2Var, uVar);
        this.j = l1Var;
    }

    m1(m1 m1Var, j$.util.u uVar) {
        super(m1Var, uVar);
        this.j = m1Var.j;
    }

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

    @Override // j$.util.stream.f
    public f f(j$.util.u uVar) {
        return new m1(this, uVar);
    }

    @Override // j$.util.stream.d
    protected Object k() {
        boolean z;
        z = this.j.b.b;
        return Boolean.valueOf(!z);
    }
}
