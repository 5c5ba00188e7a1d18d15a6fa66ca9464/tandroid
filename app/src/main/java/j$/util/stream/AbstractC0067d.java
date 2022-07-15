package j$.util.stream;

import java.util.concurrent.atomic.AtomicReference;
/* renamed from: j$.util.stream.d */
/* loaded from: classes2.dex */
abstract class AbstractC0067d extends AbstractC0079f {
    protected final AtomicReference h;
    protected volatile boolean i;

    public AbstractC0067d(AbstractC0067d abstractC0067d, j$.util.u uVar) {
        super(abstractC0067d, uVar);
        this.h = abstractC0067d.h;
    }

    public AbstractC0067d(AbstractC0188y2 abstractC0188y2, j$.util.u uVar) {
        super(abstractC0188y2, uVar);
        this.h = new AtomicReference(null);
    }

    @Override // j$.util.stream.AbstractC0079f
    public Object b() {
        if (e()) {
            Object obj = this.h.get();
            return obj == null ? k() : obj;
        }
        return super.b();
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x006b, code lost:
        r8 = r7.a();
     */
    @Override // j$.util.stream.AbstractC0079f, java.util.concurrent.CountedCompleter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void compute() {
        Object obj;
        j$.util.u trySplit;
        j$.util.u uVar = this.b;
        long estimateSize = uVar.estimateSize();
        long j = this.c;
        if (j == 0) {
            j = AbstractC0079f.h(estimateSize);
            this.c = j;
        }
        boolean z = false;
        AtomicReference atomicReference = this.h;
        AbstractC0067d abstractC0067d = this;
        while (true) {
            obj = atomicReference.get();
            if (obj != null) {
                break;
            }
            boolean z2 = abstractC0067d.i;
            if (!z2) {
                AbstractC0079f c = abstractC0067d.c();
                while (true) {
                    AbstractC0067d abstractC0067d2 = (AbstractC0067d) c;
                    if (z2 || abstractC0067d2 == null) {
                        break;
                    }
                    z2 = abstractC0067d2.i;
                    c = abstractC0067d2.c();
                }
            }
            if (z2) {
                obj = abstractC0067d.k();
                break;
            } else if (estimateSize <= j || (trySplit = uVar.trySplit()) == null) {
                break;
            } else {
                AbstractC0067d abstractC0067d3 = (AbstractC0067d) abstractC0067d.f(trySplit);
                abstractC0067d.d = abstractC0067d3;
                AbstractC0067d abstractC0067d4 = (AbstractC0067d) abstractC0067d.f(uVar);
                abstractC0067d.e = abstractC0067d4;
                abstractC0067d.setPendingCount(1);
                if (z) {
                    uVar = trySplit;
                    abstractC0067d = abstractC0067d3;
                    abstractC0067d3 = abstractC0067d4;
                } else {
                    abstractC0067d = abstractC0067d4;
                }
                z = !z;
                abstractC0067d3.fork();
                estimateSize = uVar.estimateSize();
            }
        }
        abstractC0067d.g(obj);
        abstractC0067d.tryComplete();
    }

    @Override // j$.util.stream.AbstractC0079f
    public void g(Object obj) {
        if (!e()) {
            super.g(obj);
        } else if (obj == null) {
        } else {
            this.h.compareAndSet(null, obj);
        }
    }

    @Override // j$.util.stream.AbstractC0079f, java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
    public Object getRawResult() {
        return b();
    }

    protected void i() {
        this.i = true;
    }

    public void j() {
        AbstractC0067d abstractC0067d = this;
        for (AbstractC0067d abstractC0067d2 = (AbstractC0067d) c(); abstractC0067d2 != null; abstractC0067d2 = (AbstractC0067d) abstractC0067d2.c()) {
            if (abstractC0067d2.d == abstractC0067d) {
                AbstractC0067d abstractC0067d3 = (AbstractC0067d) abstractC0067d2.e;
                if (!abstractC0067d3.i) {
                    abstractC0067d3.i();
                }
            }
            abstractC0067d = abstractC0067d2;
        }
    }

    protected abstract Object k();

    public void l(Object obj) {
        if (obj != null) {
            this.h.compareAndSet(null, obj);
        }
    }
}
