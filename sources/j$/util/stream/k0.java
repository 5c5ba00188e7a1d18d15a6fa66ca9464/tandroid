package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class k0 extends d {
    private final e0 j;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k0(e0 e0Var, z2 z2Var, j$.util.t tVar) {
        super(z2Var, tVar);
        this.j = e0Var;
    }

    k0(k0 k0Var, j$.util.t tVar) {
        super(k0Var, tVar);
        this.j = k0Var.j;
    }

    private void m(Object obj) {
        boolean z;
        k0 k0Var = this;
        while (true) {
            if (k0Var != null) {
                f c = k0Var.c();
                if (c != null && c.d != k0Var) {
                    z = false;
                    break;
                }
                k0Var = c;
            } else {
                z = true;
                break;
            }
        }
        if (z) {
            l(obj);
        } else {
            j();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public Object a() {
        z2 z2Var = this.a;
        P4 p4 = (P4) this.j.e.get();
        z2Var.s0(p4, this.b);
        Object obj = p4.get();
        if (!this.j.b) {
            if (obj != null) {
                l(obj);
            }
            return null;
        } else if (obj != null) {
            m(obj);
            return obj;
        } else {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public f f(j$.util.t tVar) {
        return new k0(this, tVar);
    }

    @Override // j$.util.stream.d
    protected Object k() {
        return this.j.c;
    }

    @Override // j$.util.stream.f, java.util.concurrent.CountedCompleter
    public void onCompletion(CountedCompleter countedCompleter) {
        if (this.j.b) {
            k0 k0Var = (k0) this.d;
            k0 k0Var2 = null;
            while (true) {
                if (k0Var != k0Var2) {
                    Object b = k0Var.b();
                    if (b != null && this.j.d.test(b)) {
                        g(b);
                        m(b);
                        break;
                    }
                    k0Var2 = k0Var;
                    k0Var = (k0) this.e;
                } else {
                    break;
                }
            }
        }
        this.b = null;
        this.e = null;
        this.d = null;
    }
}
