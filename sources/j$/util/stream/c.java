package j$.util.stream;

import java.util.concurrent.CountedCompleter;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes2.dex */
abstract class c extends e {
    protected final AtomicReference h;
    protected volatile boolean i;

    /* JADX INFO: Access modifiers changed from: protected */
    public c(b bVar, j$.util.Q q) {
        super(bVar, q);
        this.h = new AtomicReference(null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public c(c cVar, j$.util.Q q) {
        super(cVar, q);
        this.h = cVar.h;
    }

    @Override // j$.util.stream.e
    public final Object b() {
        if (!c()) {
            return super.b();
        }
        Object obj = this.h.get();
        return obj == null ? i() : obj;
    }

    @Override // j$.util.stream.e, java.util.concurrent.CountedCompleter
    public final void compute() {
        Object obj;
        j$.util.Q trySplit;
        j$.util.Q q = this.b;
        long estimateSize = q.estimateSize();
        long j = this.c;
        if (j == 0) {
            j = e.f(estimateSize);
            this.c = j;
        }
        AtomicReference atomicReference = this.h;
        boolean z = false;
        c cVar = this;
        while (true) {
            obj = atomicReference.get();
            if (obj != null) {
                break;
            }
            boolean z2 = cVar.i;
            if (!z2) {
                CountedCompleter<?> completer = cVar.getCompleter();
                while (true) {
                    c cVar2 = (c) ((e) completer);
                    if (z2 || cVar2 == null) {
                        break;
                    }
                    z2 = cVar2.i;
                    completer = cVar2.getCompleter();
                }
            }
            if (z2) {
                obj = cVar.i();
                break;
            }
            if (estimateSize <= j || (trySplit = q.trySplit()) == null) {
                break;
            }
            c cVar3 = (c) cVar.d(trySplit);
            cVar.d = cVar3;
            c cVar4 = (c) cVar.d(q);
            cVar.e = cVar4;
            cVar.setPendingCount(1);
            if (z) {
                q = trySplit;
                cVar = cVar3;
                cVar3 = cVar4;
            } else {
                cVar = cVar4;
            }
            z = !z;
            cVar3.fork();
            estimateSize = q.estimateSize();
        }
        obj = cVar.a();
        cVar.e(obj);
        cVar.tryComplete();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.e
    public final void e(Object obj) {
        if (!c()) {
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

    @Override // j$.util.stream.e, java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
    public final Object getRawResult() {
        return b();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void h() {
        c cVar = this;
        for (c cVar2 = (c) ((e) getCompleter()); cVar2 != null; cVar2 = (c) ((e) cVar2.getCompleter())) {
            if (cVar2.d == cVar) {
                c cVar3 = (c) cVar2.e;
                if (!cVar3.i) {
                    cVar3.g();
                }
            }
            cVar = cVar2;
        }
    }

    protected abstract Object i();
}
