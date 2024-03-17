package j$.util.stream;

import j$.util.concurrent.ConcurrentHashMap;
import java.util.Objects;
import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class q0 extends CountedCompleter {
    public static final /* synthetic */ int h = 0;
    private final y2 a;
    private j$.util.t b;
    private final long c;
    private final ConcurrentHashMap d;
    private final m3 e;
    private final q0 f;
    private A1 g;

    q0(q0 q0Var, j$.util.t tVar, q0 q0Var2) {
        super(q0Var);
        this.a = q0Var.a;
        this.b = tVar;
        this.c = q0Var.c;
        this.d = q0Var.d;
        this.e = q0Var.e;
        this.f = q0Var2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public q0(y2 y2Var, j$.util.t tVar, m3 m3Var) {
        super(null);
        this.a = y2Var;
        this.b = tVar;
        this.c = f.h(tVar.estimateSize());
        this.d = new ConcurrentHashMap(Math.max(16, f.g << 1));
        this.e = m3Var;
        this.f = null;
    }

    @Override // java.util.concurrent.CountedCompleter
    public final void compute() {
        j$.util.t trySplit;
        j$.util.t tVar = this.b;
        long j = this.c;
        boolean z = false;
        q0 q0Var = this;
        while (tVar.estimateSize() > j && (trySplit = tVar.trySplit()) != null) {
            q0 q0Var2 = new q0(q0Var, trySplit, q0Var.f);
            q0 q0Var3 = new q0(q0Var, tVar, q0Var2);
            q0Var.addToPendingCount(1);
            q0Var3.addToPendingCount(1);
            q0Var.d.put(q0Var2, q0Var3);
            if (q0Var.f != null) {
                q0Var2.addToPendingCount(1);
                if (q0Var.d.replace(q0Var.f, q0Var, q0Var2)) {
                    q0Var.addToPendingCount(-1);
                } else {
                    q0Var2.addToPendingCount(-1);
                }
            }
            if (z) {
                tVar = trySplit;
                q0Var = q0Var2;
                q0Var2 = q0Var3;
            } else {
                q0Var = q0Var3;
            }
            z = !z;
            q0Var2.fork();
        }
        if (q0Var.getPendingCount() > 0) {
            p0 p0Var = new j$.util.function.m() { // from class: j$.util.stream.p0
                @Override // j$.util.function.m
                public final Object apply(int i) {
                    int i2 = q0.h;
                    return new Object[i];
                }
            };
            y2 y2Var = q0Var.a;
            s1 t0 = y2Var.t0(y2Var.q0(tVar), p0Var);
            c cVar = (c) q0Var.a;
            Objects.requireNonNull(cVar);
            Objects.requireNonNull(t0);
            cVar.n0(cVar.v0(t0), tVar);
            q0Var.g = t0.a();
            q0Var.b = null;
        }
        q0Var.tryComplete();
    }

    @Override // java.util.concurrent.CountedCompleter
    public void onCompletion(CountedCompleter countedCompleter) {
        A1 a1 = this.g;
        if (a1 != null) {
            a1.forEach(this.e);
            this.g = null;
        } else {
            j$.util.t tVar = this.b;
            if (tVar != null) {
                y2 y2Var = this.a;
                m3 m3Var = this.e;
                c cVar = (c) y2Var;
                Objects.requireNonNull(cVar);
                Objects.requireNonNull(m3Var);
                cVar.n0(cVar.v0(m3Var), tVar);
                this.b = null;
            }
        }
        q0 q0Var = (q0) this.d.remove(this);
        if (q0Var != null) {
            q0Var.tryComplete();
        }
    }
}
