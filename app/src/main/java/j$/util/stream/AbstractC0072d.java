package j$.util.stream;

import java.util.concurrent.atomic.AtomicReference;
/* renamed from: j$.util.stream.d */
/* loaded from: classes2.dex */
abstract class AbstractC0072d extends AbstractC0084f {
    protected final AtomicReference h;
    protected volatile boolean i;

    public AbstractC0072d(AbstractC0072d abstractC0072d, j$.util.u uVar) {
        super(abstractC0072d, uVar);
        this.h = abstractC0072d.h;
    }

    public AbstractC0072d(AbstractC0193y2 abstractC0193y2, j$.util.u uVar) {
        super(abstractC0193y2, uVar);
        this.h = new AtomicReference(null);
    }

    @Override // j$.util.stream.AbstractC0084f
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
    @Override // j$.util.stream.AbstractC0084f, java.util.concurrent.CountedCompleter
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
            j = AbstractC0084f.h(estimateSize);
            this.c = j;
        }
        boolean z = false;
        AtomicReference atomicReference = this.h;
        AbstractC0072d abstractC0072d = this;
        while (true) {
            obj = atomicReference.get();
            if (obj != null) {
                break;
            }
            boolean z2 = abstractC0072d.i;
            if (!z2) {
                AbstractC0084f c = abstractC0072d.c();
                while (true) {
                    AbstractC0072d abstractC0072d2 = (AbstractC0072d) c;
                    if (z2 || abstractC0072d2 == null) {
                        break;
                    }
                    z2 = abstractC0072d2.i;
                    c = abstractC0072d2.c();
                }
            }
            if (z2) {
                obj = abstractC0072d.k();
                break;
            } else if (estimateSize <= j || (trySplit = uVar.trySplit()) == null) {
                break;
            } else {
                AbstractC0072d abstractC0072d3 = (AbstractC0072d) abstractC0072d.f(trySplit);
                abstractC0072d.d = abstractC0072d3;
                AbstractC0072d abstractC0072d4 = (AbstractC0072d) abstractC0072d.f(uVar);
                abstractC0072d.e = abstractC0072d4;
                abstractC0072d.setPendingCount(1);
                if (z) {
                    uVar = trySplit;
                    abstractC0072d = abstractC0072d3;
                    abstractC0072d3 = abstractC0072d4;
                } else {
                    abstractC0072d = abstractC0072d4;
                }
                z = !z;
                abstractC0072d3.fork();
                estimateSize = uVar.estimateSize();
            }
        }
        abstractC0072d.g(obj);
        abstractC0072d.tryComplete();
    }

    @Override // j$.util.stream.AbstractC0084f
    public void g(Object obj) {
        if (!e()) {
            super.g(obj);
        } else if (obj == null) {
        } else {
            this.h.compareAndSet(null, obj);
        }
    }

    @Override // j$.util.stream.AbstractC0084f, java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
    public Object getRawResult() {
        return b();
    }

    protected void i() {
        this.i = true;
    }

    public void j() {
        AbstractC0072d abstractC0072d = this;
        for (AbstractC0072d abstractC0072d2 = (AbstractC0072d) c(); abstractC0072d2 != null; abstractC0072d2 = (AbstractC0072d) abstractC0072d2.c()) {
            if (abstractC0072d2.d == abstractC0072d) {
                AbstractC0072d abstractC0072d3 = (AbstractC0072d) abstractC0072d2.e;
                if (!abstractC0072d3.i) {
                    abstractC0072d3.i();
                }
            }
            abstractC0072d = abstractC0072d2;
        }
    }

    protected abstract Object k();

    public void l(Object obj) {
        if (obj != null) {
            this.h.compareAndSet(null, obj);
        }
    }
}
