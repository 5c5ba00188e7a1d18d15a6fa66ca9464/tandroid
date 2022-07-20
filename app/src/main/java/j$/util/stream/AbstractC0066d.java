package j$.util.stream;

import java.util.concurrent.atomic.AtomicReference;
/* renamed from: j$.util.stream.d */
/* loaded from: classes2.dex */
abstract class AbstractC0066d extends AbstractC0078f {
    protected final AtomicReference h;
    protected volatile boolean i;

    public AbstractC0066d(AbstractC0066d abstractC0066d, j$.util.u uVar) {
        super(abstractC0066d, uVar);
        this.h = abstractC0066d.h;
    }

    public AbstractC0066d(AbstractC0187y2 abstractC0187y2, j$.util.u uVar) {
        super(abstractC0187y2, uVar);
        this.h = new AtomicReference(null);
    }

    @Override // j$.util.stream.AbstractC0078f
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
    @Override // j$.util.stream.AbstractC0078f, java.util.concurrent.CountedCompleter
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
            j = AbstractC0078f.h(estimateSize);
            this.c = j;
        }
        boolean z = false;
        AtomicReference atomicReference = this.h;
        AbstractC0066d abstractC0066d = this;
        while (true) {
            obj = atomicReference.get();
            if (obj != null) {
                break;
            }
            boolean z2 = abstractC0066d.i;
            if (!z2) {
                AbstractC0078f c = abstractC0066d.c();
                while (true) {
                    AbstractC0066d abstractC0066d2 = (AbstractC0066d) c;
                    if (z2 || abstractC0066d2 == null) {
                        break;
                    }
                    z2 = abstractC0066d2.i;
                    c = abstractC0066d2.c();
                }
            }
            if (z2) {
                obj = abstractC0066d.k();
                break;
            } else if (estimateSize <= j || (trySplit = uVar.trySplit()) == null) {
                break;
            } else {
                AbstractC0066d abstractC0066d3 = (AbstractC0066d) abstractC0066d.f(trySplit);
                abstractC0066d.d = abstractC0066d3;
                AbstractC0066d abstractC0066d4 = (AbstractC0066d) abstractC0066d.f(uVar);
                abstractC0066d.e = abstractC0066d4;
                abstractC0066d.setPendingCount(1);
                if (z) {
                    uVar = trySplit;
                    abstractC0066d = abstractC0066d3;
                    abstractC0066d3 = abstractC0066d4;
                } else {
                    abstractC0066d = abstractC0066d4;
                }
                z = !z;
                abstractC0066d3.fork();
                estimateSize = uVar.estimateSize();
            }
        }
        abstractC0066d.g(obj);
        abstractC0066d.tryComplete();
    }

    @Override // j$.util.stream.AbstractC0078f
    public void g(Object obj) {
        if (!e()) {
            super.g(obj);
        } else if (obj == null) {
        } else {
            this.h.compareAndSet(null, obj);
        }
    }

    @Override // j$.util.stream.AbstractC0078f, java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
    public Object getRawResult() {
        return b();
    }

    protected void i() {
        this.i = true;
    }

    public void j() {
        AbstractC0066d abstractC0066d = this;
        for (AbstractC0066d abstractC0066d2 = (AbstractC0066d) c(); abstractC0066d2 != null; abstractC0066d2 = (AbstractC0066d) abstractC0066d2.c()) {
            if (abstractC0066d2.d == abstractC0066d) {
                AbstractC0066d abstractC0066d3 = (AbstractC0066d) abstractC0066d2.e;
                if (!abstractC0066d3.i) {
                    abstractC0066d3.i();
                }
            }
            abstractC0066d = abstractC0066d2;
        }
    }

    protected abstract Object k();

    public void l(Object obj) {
        if (obj != null) {
            this.h.compareAndSet(null, obj);
        }
    }
}
