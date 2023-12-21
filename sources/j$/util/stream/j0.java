package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class j0 extends d {
    private final d0 j;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j0(d0 d0Var, y2 y2Var, j$.util.t tVar) {
        super(y2Var, tVar);
        this.j = d0Var;
    }

    j0(j0 j0Var, j$.util.t tVar) {
        super(j0Var, tVar);
        this.j = j0Var.j;
    }

    private void m(Object obj) {
        boolean z;
        j0 j0Var = this;
        while (true) {
            if (j0Var != null) {
                f c = j0Var.c();
                if (c != null && c.d != j0Var) {
                    z = false;
                    break;
                }
                j0Var = c;
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
        y2 y2Var = this.a;
        O4 o4 = (O4) this.j.e.get();
        y2Var.u0(o4, this.b);
        Object obj = o4.get();
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
        return new j0(this, tVar);
    }

    @Override // j$.util.stream.d
    protected Object k() {
        return this.j.c;
    }

    @Override // j$.util.stream.f, java.util.concurrent.CountedCompleter
    public void onCompletion(CountedCompleter countedCompleter) {
        if (this.j.b) {
            j0 j0Var = (j0) this.d;
            j0 j0Var2 = null;
            while (true) {
                if (j0Var != j0Var2) {
                    Object b = j0Var.b();
                    if (b != null && this.j.d.test(b)) {
                        g(b);
                        m(b);
                        break;
                    }
                    j0Var2 = j0Var;
                    j0Var = (j0) this.e;
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
