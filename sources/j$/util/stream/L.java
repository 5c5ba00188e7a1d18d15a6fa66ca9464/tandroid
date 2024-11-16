package j$.util.stream;

import java.util.concurrent.CountedCompleter;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes2.dex */
final class L extends c {
    private final F j;

    L(F f, b bVar, j$.util.Q q) {
        super(bVar, q);
        this.j = f;
    }

    L(L l, j$.util.Q q) {
        super(l, q);
        this.j = l.j;
    }

    @Override // j$.util.stream.e
    protected final Object a() {
        b bVar = this.a;
        y3 y3Var = (y3) this.j.d.get();
        bVar.D0(this.b, y3Var);
        Object obj = y3Var.get();
        if (!this.j.a) {
            if (obj != null) {
                AtomicReference atomicReference = this.h;
                while (!atomicReference.compareAndSet(null, obj) && atomicReference.get() == null) {
                }
            }
            return null;
        }
        if (obj == null) {
            return null;
        }
        e eVar = this;
        while (true) {
            if (eVar != null) {
                e eVar2 = (e) eVar.getCompleter();
                if (eVar2 != null && eVar2.d != eVar) {
                    h();
                    break;
                }
                eVar = eVar2;
            } else {
                AtomicReference atomicReference2 = this.h;
                while (!atomicReference2.compareAndSet(null, obj) && atomicReference2.get() == null) {
                }
            }
        }
        return obj;
    }

    @Override // j$.util.stream.e
    protected final e d(j$.util.Q q) {
        return new L(this, q);
    }

    @Override // j$.util.stream.c
    protected final Object i() {
        return this.j.b;
    }

    @Override // j$.util.stream.e, java.util.concurrent.CountedCompleter
    public final void onCompletion(CountedCompleter countedCompleter) {
        if (this.j.a) {
            L l = (L) this.d;
            L l2 = null;
            while (true) {
                if (l != l2) {
                    Object b = l.b();
                    if (b != null && this.j.c.test(b)) {
                        e(b);
                        e eVar = this;
                        while (true) {
                            if (eVar != null) {
                                e eVar2 = (e) eVar.getCompleter();
                                if (eVar2 != null && eVar2.d != eVar) {
                                    h();
                                    break;
                                }
                                eVar = eVar2;
                            } else {
                                AtomicReference atomicReference = this.h;
                                while (!atomicReference.compareAndSet(null, b) && atomicReference.get() == null) {
                                }
                            }
                        }
                    } else {
                        l2 = l;
                        l = (L) this.e;
                    }
                } else {
                    break;
                }
            }
        }
        super.onCompletion(countedCompleter);
    }
}
