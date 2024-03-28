package j$.util.stream;

import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes2.dex */
abstract class d extends f {
    protected final AtomicReference h;
    protected volatile boolean i;

    /* JADX INFO: Access modifiers changed from: protected */
    public d(d dVar, j$.util.t tVar) {
        super(dVar, tVar);
        this.h = dVar.h;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public d(z2 z2Var, j$.util.t tVar) {
        super(z2Var, tVar);
        this.h = new AtomicReference(null);
    }

    @Override // j$.util.stream.f
    public Object b() {
        if (e()) {
            Object obj = this.h.get();
            return obj == null ? k() : obj;
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
    public void compute() {
        Object obj;
        j$.util.t trySplit;
        j$.util.t tVar = this.b;
        long estimateSize = tVar.estimateSize();
        long j = this.c;
        if (j == 0) {
            j = f.h(estimateSize);
            this.c = j;
        }
        boolean z = false;
        AtomicReference atomicReference = this.h;
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
                obj = dVar.k();
                break;
            } else if (estimateSize <= j || (trySplit = tVar.trySplit()) == null) {
                break;
            } else {
                d dVar3 = (d) dVar.f(trySplit);
                dVar.d = dVar3;
                d dVar4 = (d) dVar.f(tVar);
                dVar.e = dVar4;
                dVar.setPendingCount(1);
                if (z) {
                    tVar = trySplit;
                    dVar = dVar3;
                    dVar3 = dVar4;
                } else {
                    dVar = dVar4;
                }
                z = !z;
                dVar3.fork();
                estimateSize = tVar.estimateSize();
            }
        }
        dVar.g(obj);
        dVar.tryComplete();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public void g(Object obj) {
        if (!e()) {
            super.g(obj);
        } else if (obj != null) {
            this.h.compareAndSet(null, obj);
        }
    }

    @Override // j$.util.stream.f, java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
    public Object getRawResult() {
        return b();
    }

    protected void i() {
        this.i = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void j() {
        d dVar = this;
        for (d dVar2 = (d) c(); dVar2 != null; dVar2 = (d) dVar2.c()) {
            if (dVar2.d == dVar) {
                d dVar3 = (d) dVar2.e;
                if (!dVar3.i) {
                    dVar3.i();
                }
            }
            dVar = dVar2;
        }
    }

    protected abstract Object k();

    /* JADX INFO: Access modifiers changed from: protected */
    public void l(Object obj) {
        if (obj != null) {
            this.h.compareAndSet(null, obj);
        }
    }
}
