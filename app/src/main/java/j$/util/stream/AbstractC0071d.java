package j$.util.stream;

import java.util.concurrent.atomic.AtomicReference;
/* renamed from: j$.util.stream.d */
/* loaded from: classes2.dex */
abstract class AbstractC0071d extends AbstractC0083f {
    protected final AtomicReference h;
    protected volatile boolean i;

    public AbstractC0071d(AbstractC0071d abstractC0071d, j$.util.u uVar) {
        super(abstractC0071d, uVar);
        this.h = abstractC0071d.h;
    }

    public AbstractC0071d(AbstractC0192y2 abstractC0192y2, j$.util.u uVar) {
        super(abstractC0192y2, uVar);
        this.h = new AtomicReference(null);
    }

    @Override // j$.util.stream.AbstractC0083f
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
    @Override // j$.util.stream.AbstractC0083f, java.util.concurrent.CountedCompleter
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
            j = AbstractC0083f.h(estimateSize);
            this.c = j;
        }
        boolean z = false;
        AtomicReference atomicReference = this.h;
        AbstractC0071d abstractC0071d = this;
        while (true) {
            obj = atomicReference.get();
            if (obj != null) {
                break;
            }
            boolean z2 = abstractC0071d.i;
            if (!z2) {
                AbstractC0083f c = abstractC0071d.c();
                while (true) {
                    AbstractC0071d abstractC0071d2 = (AbstractC0071d) c;
                    if (z2 || abstractC0071d2 == null) {
                        break;
                    }
                    z2 = abstractC0071d2.i;
                    c = abstractC0071d2.c();
                }
            }
            if (z2) {
                obj = abstractC0071d.k();
                break;
            } else if (estimateSize <= j || (trySplit = uVar.trySplit()) == null) {
                break;
            } else {
                AbstractC0071d abstractC0071d3 = (AbstractC0071d) abstractC0071d.f(trySplit);
                abstractC0071d.d = abstractC0071d3;
                AbstractC0071d abstractC0071d4 = (AbstractC0071d) abstractC0071d.f(uVar);
                abstractC0071d.e = abstractC0071d4;
                abstractC0071d.setPendingCount(1);
                if (z) {
                    uVar = trySplit;
                    abstractC0071d = abstractC0071d3;
                    abstractC0071d3 = abstractC0071d4;
                } else {
                    abstractC0071d = abstractC0071d4;
                }
                z = !z;
                abstractC0071d3.fork();
                estimateSize = uVar.estimateSize();
            }
        }
        abstractC0071d.g(obj);
        abstractC0071d.tryComplete();
    }

    @Override // j$.util.stream.AbstractC0083f
    public void g(Object obj) {
        if (!e()) {
            super.g(obj);
        } else if (obj == null) {
        } else {
            this.h.compareAndSet(null, obj);
        }
    }

    @Override // j$.util.stream.AbstractC0083f, java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
    public Object getRawResult() {
        return b();
    }

    protected void i() {
        this.i = true;
    }

    public void j() {
        AbstractC0071d abstractC0071d = this;
        for (AbstractC0071d abstractC0071d2 = (AbstractC0071d) c(); abstractC0071d2 != null; abstractC0071d2 = (AbstractC0071d) abstractC0071d2.c()) {
            if (abstractC0071d2.d == abstractC0071d) {
                AbstractC0071d abstractC0071d3 = (AbstractC0071d) abstractC0071d2.e;
                if (!abstractC0071d3.i) {
                    abstractC0071d3.i();
                }
            }
            abstractC0071d = abstractC0071d2;
        }
    }

    protected abstract Object k();

    public void l(Object obj) {
        if (obj != null) {
            this.h.compareAndSet(null, obj);
        }
    }
}
