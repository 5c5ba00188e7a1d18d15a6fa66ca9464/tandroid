package j$.util.stream;

import j$.util.concurrent.ConcurrentHashMap;
import java.util.Objects;
import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class r0 extends CountedCompleter {
    public static final /* synthetic */ int h = 0;
    private final y2 a;
    private j$.util.s b;
    private final long c;
    private final ConcurrentHashMap d;
    private final m3 e;
    private final r0 f;
    private A1 g;

    r0(r0 r0Var, j$.util.s sVar, r0 r0Var2) {
        super(r0Var);
        this.a = r0Var.a;
        this.b = sVar;
        this.c = r0Var.c;
        this.d = r0Var.d;
        this.e = r0Var.e;
        this.f = r0Var2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public r0(y2 y2Var, j$.util.s sVar, m3 m3Var) {
        super(null);
        this.a = y2Var;
        this.b = sVar;
        this.c = f.h(sVar.estimateSize());
        this.d = new ConcurrentHashMap(Math.max(16, f.g << 1));
        this.e = m3Var;
        this.f = null;
    }

    @Override // java.util.concurrent.CountedCompleter
    public final void compute() {
        j$.util.s trySplit;
        j$.util.s sVar = this.b;
        long j = this.c;
        boolean z = false;
        r0 r0Var = this;
        while (sVar.estimateSize() > j && (trySplit = sVar.trySplit()) != null) {
            r0 r0Var2 = new r0(r0Var, trySplit, r0Var.f);
            r0 r0Var3 = new r0(r0Var, sVar, r0Var2);
            r0Var.addToPendingCount(1);
            r0Var3.addToPendingCount(1);
            r0Var.d.put(r0Var2, r0Var3);
            if (r0Var.f != null) {
                r0Var2.addToPendingCount(1);
                if (r0Var.d.replace(r0Var.f, r0Var, r0Var2)) {
                    r0Var.addToPendingCount(-1);
                } else {
                    r0Var2.addToPendingCount(-1);
                }
            }
            if (z) {
                sVar = trySplit;
                r0Var = r0Var2;
                r0Var2 = r0Var3;
            } else {
                r0Var = r0Var3;
            }
            z = !z;
            r0Var2.fork();
        }
        if (r0Var.getPendingCount() > 0) {
            q0 q0Var = new j$.util.function.m() { // from class: j$.util.stream.q0
                @Override // j$.util.function.m
                public final Object apply(int i) {
                    int i2 = r0.h;
                    return new Object[i];
                }
            };
            y2 y2Var = r0Var.a;
            s1 o0 = y2Var.o0(y2Var.l0(sVar), q0Var);
            c cVar = (c) r0Var.a;
            Objects.requireNonNull(cVar);
            Objects.requireNonNull(o0);
            cVar.i0(cVar.q0(o0), sVar);
            r0Var.g = o0.a();
            r0Var.b = null;
        }
        r0Var.tryComplete();
    }

    @Override // java.util.concurrent.CountedCompleter
    public void onCompletion(CountedCompleter countedCompleter) {
        A1 a1 = this.g;
        if (a1 != null) {
            a1.forEach(this.e);
            this.g = null;
        } else {
            j$.util.s sVar = this.b;
            if (sVar != null) {
                y2 y2Var = this.a;
                m3 m3Var = this.e;
                c cVar = (c) y2Var;
                Objects.requireNonNull(cVar);
                Objects.requireNonNull(m3Var);
                cVar.i0(cVar.q0(m3Var), sVar);
                this.b = null;
            }
        }
        r0 r0Var = (r0) this.d.remove(this);
        if (r0Var != null) {
            r0Var.tryComplete();
        }
    }
}
