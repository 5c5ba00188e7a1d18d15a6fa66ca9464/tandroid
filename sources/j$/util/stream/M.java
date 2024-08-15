package j$.util.stream;

import java.util.concurrent.CountedCompleter;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes2.dex */
final class M extends d {
    private final G j;

    /* JADX INFO: Access modifiers changed from: package-private */
    public M(G g, u0 u0Var, j$.util.Q q) {
        super(u0Var, q);
        this.j = g;
    }

    M(M m, j$.util.Q q) {
        super(m, q);
        this.j = m.j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public final Object a() {
        boolean z;
        u0 u0Var = this.a;
        D3 d3 = (D3) this.j.d.get();
        u0Var.X0(this.b, d3);
        Object obj = d3.get();
        if (!this.j.a) {
            if (obj != null) {
                AtomicReference atomicReference = this.h;
                while (!atomicReference.compareAndSet(null, obj) && atomicReference.get() == null) {
                }
            }
            return null;
        } else if (obj != null) {
            M m = this;
            while (true) {
                if (m != null) {
                    f c = m.c();
                    if (c != null && c.d != m) {
                        z = false;
                        break;
                    }
                    m = c;
                } else {
                    z = true;
                    break;
                }
            }
            if (z) {
                AtomicReference atomicReference2 = this.h;
                while (!atomicReference2.compareAndSet(null, obj) && atomicReference2.get() == null) {
                }
            } else {
                h();
            }
            return obj;
        } else {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public final f d(j$.util.Q q) {
        return new M(this, q);
    }

    @Override // j$.util.stream.d
    protected final Object i() {
        return this.j.b;
    }

    @Override // j$.util.stream.f, java.util.concurrent.CountedCompleter
    public final void onCompletion(CountedCompleter countedCompleter) {
        boolean z;
        if (this.j.a) {
            M m = (M) this.d;
            M m2 = null;
            while (true) {
                if (m == m2) {
                    break;
                }
                Object b = m.b();
                if (b == null || !this.j.c.test(b)) {
                    m2 = m;
                    m = (M) this.e;
                } else {
                    e(b);
                    M m3 = this;
                    while (true) {
                        if (m3 != null) {
                            f c = m3.c();
                            if (c != null && c.d != m3) {
                                z = false;
                                break;
                            }
                            m3 = c;
                        } else {
                            z = true;
                            break;
                        }
                    }
                    if (z) {
                        AtomicReference atomicReference = this.h;
                        while (!atomicReference.compareAndSet(null, b) && atomicReference.get() == null) {
                        }
                    } else {
                        h();
                    }
                }
            }
        }
        super.onCompletion(countedCompleter);
    }
}
