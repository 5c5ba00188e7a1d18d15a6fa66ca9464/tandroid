package j$.util.stream;

import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes2.dex */
abstract class d extends f {
    protected final AtomicReference h;
    protected volatile boolean i;

    /* JADX INFO: Access modifiers changed from: protected */
    public d(d dVar, j$.util.Q q) {
        super(dVar, q);
        this.h = dVar.h;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public d(u0 u0Var, j$.util.Q q) {
        super(u0Var, q);
        this.h = new AtomicReference(null);
    }

    @Override // j$.util.stream.f
    public final Object b() {
        if (c() == null) {
            Object obj = this.h.get();
            return obj == null ? i() : obj;
        }
        return super.b();
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x006b, code lost:
        r8 = r7.a();
     */
    @Override // j$.util.stream.f, java.util.concurrent.CountedCompleter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void compute() {
        Object obj;
        j$.util.Q trySplit;
        j$.util.Q q = this.b;
        long estimateSize = q.estimateSize();
        long j = this.c;
        if (j == 0) {
            j = f.f(estimateSize);
            this.c = j;
        }
        AtomicReference atomicReference = this.h;
        boolean z = false;
        d dVar = this;
        while (true) {
            obj = atomicReference.get();
            if (obj != null) {
                break;
            }
            boolean z2 = dVar.i;
            if (!z2) {
                f c = dVar.c();
                while (true) {
                    d dVar2 = (d) c;
                    if (z2 || dVar2 == null) {
                        break;
                    }
                    z2 = dVar2.i;
                    c = dVar2.c();
                }
            }
            if (z2) {
                obj = dVar.i();
                break;
            } else if (estimateSize <= j || (trySplit = q.trySplit()) == null) {
                break;
            } else {
                d dVar3 = (d) dVar.d(trySplit);
                dVar.d = dVar3;
                d dVar4 = (d) dVar.d(q);
                dVar.e = dVar4;
                dVar.setPendingCount(1);
                if (z) {
                    q = trySplit;
                    dVar = dVar3;
                    dVar3 = dVar4;
                } else {
                    dVar = dVar4;
                }
                z = !z;
                dVar3.fork();
                estimateSize = q.estimateSize();
            }
        }
        dVar.e(obj);
        dVar.tryComplete();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public final void e(Object obj) {
        if (!(c() == null)) {
            super.e(obj);
        } else if (obj != null) {
            AtomicReference atomicReference = this.h;
            while (!atomicReference.compareAndSet(null, obj) && atomicReference.get() == null) {
            }
        }
    }

    protected void g() {
        this.i = true;
    }

    @Override // j$.util.stream.f, java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
    public final Object getRawResult() {
        return b();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void h() {
        d dVar = this;
        for (d dVar2 = (d) c(); dVar2 != null; dVar2 = (d) dVar2.c()) {
            if (dVar2.d == dVar) {
                d dVar3 = (d) dVar2.e;
                if (!dVar3.i) {
                    dVar3.g();
                }
            }
            dVar = dVar2;
        }
    }

    protected abstract Object i();
}
