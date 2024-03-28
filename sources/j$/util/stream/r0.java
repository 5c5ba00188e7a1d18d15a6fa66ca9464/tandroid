package j$.util.stream;

import j$.util.concurrent.ConcurrentHashMap;
import java.util.Objects;
import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class r0 extends CountedCompleter {
    public static final /* synthetic */ int h = 0;
    private final z2 a;
    private j$.util.t b;
    private final long c;
    private final ConcurrentHashMap d;
    private final n3 e;
    private final r0 f;
    private B1 g;

    r0(r0 r0Var, j$.util.t tVar, r0 r0Var2) {
        super(r0Var);
        this.a = r0Var.a;
        this.b = tVar;
        this.c = r0Var.c;
        this.d = r0Var.d;
        this.e = r0Var.e;
        this.f = r0Var2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public r0(z2 z2Var, j$.util.t tVar, n3 n3Var) {
        super(null);
        this.a = z2Var;
        this.b = tVar;
        this.c = f.h(tVar.estimateSize());
        this.d = new ConcurrentHashMap(Math.max(16, f.g << 1));
        this.e = n3Var;
        this.f = null;
    }

    @Override // java.util.concurrent.CountedCompleter
    public final void compute() {
        j$.util.t trySplit;
        j$.util.t tVar = this.b;
        long j = this.c;
        boolean z = false;
        r0 r0Var = this;
        while (tVar.estimateSize() > j && (trySplit = tVar.trySplit()) != null) {
            r0 r0Var2 = new r0(r0Var, trySplit, r0Var.f);
            r0 r0Var3 = new r0(r0Var, tVar, r0Var2);
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
                tVar = trySplit;
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
            z2 z2Var = r0Var.a;
            t1 r0 = z2Var.r0(z2Var.o0(tVar), q0Var);
            c cVar = (c) r0Var.a;
            Objects.requireNonNull(cVar);
            Objects.requireNonNull(r0);
            cVar.l0(cVar.t0(r0), tVar);
            r0Var.g = r0.a();
            r0Var.b = null;
        }
        r0Var.tryComplete();
    }

    @Override // java.util.concurrent.CountedCompleter
    public void onCompletion(CountedCompleter countedCompleter) {
        B1 b1 = this.g;
        if (b1 != null) {
            b1.forEach(this.e);
            this.g = null;
        } else {
            j$.util.t tVar = this.b;
            if (tVar != null) {
                z2 z2Var = this.a;
                n3 n3Var = this.e;
                c cVar = (c) z2Var;
                Objects.requireNonNull(cVar);
                Objects.requireNonNull(n3Var);
                cVar.l0(cVar.t0(n3Var), tVar);
                this.b = null;
            }
        }
        r0 r0Var = (r0) this.d.remove(this);
        if (r0Var != null) {
            r0Var.tryComplete();
        }
    }
}
