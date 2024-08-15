package j$.util.stream;

import j$.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class T extends CountedCompleter {
    public static final /* synthetic */ int h = 0;
    private final u0 a;
    private j$.util.Q b;
    private final long c;
    private final ConcurrentHashMap d;
    private final f2 e;
    private final T f;
    private D0 g;

    T(T t, j$.util.Q q, T t2) {
        super(t);
        this.a = t.a;
        this.b = q;
        this.c = t.c;
        this.d = t.d;
        this.e = t.e;
        this.f = t2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public T(u0 u0Var, j$.util.Q q, f2 f2Var) {
        super(null);
        this.a = u0Var;
        this.b = q;
        this.c = f.f(q.estimateSize());
        this.d = new ConcurrentHashMap(Math.max(16, f.g << 1));
        this.e = f2Var;
        this.f = null;
    }

    @Override // java.util.concurrent.CountedCompleter
    public final void compute() {
        j$.util.Q trySplit;
        j$.util.Q q = this.b;
        long j = this.c;
        boolean z = false;
        T t = this;
        while (q.estimateSize() > j && (trySplit = q.trySplit()) != null) {
            T t2 = new T(t, trySplit, t.f);
            T t3 = new T(t, q, t2);
            t.addToPendingCount(1);
            t3.addToPendingCount(1);
            t.d.put(t2, t3);
            if (t.f != null) {
                t2.addToPendingCount(1);
                if (t.d.replace(t.f, t, t2)) {
                    t.addToPendingCount(-1);
                } else {
                    t2.addToPendingCount(-1);
                }
            }
            if (z) {
                q = trySplit;
                t = t2;
                t2 = t3;
            } else {
                t = t3;
            }
            z = !z;
            t2.fork();
        }
        if (t.getPendingCount() > 0) {
            b bVar = new b(15);
            u0 u0Var = t.a;
            y0 T0 = u0Var.T0(u0Var.I0(q), bVar);
            t.a.X0(q, T0);
            t.g = T0.build();
            t.b = null;
        }
        t.tryComplete();
    }

    @Override // java.util.concurrent.CountedCompleter
    public final void onCompletion(CountedCompleter countedCompleter) {
        D0 d0 = this.g;
        if (d0 != null) {
            d0.forEach(this.e);
            this.g = null;
        } else {
            j$.util.Q q = this.b;
            if (q != null) {
                this.a.X0(q, this.e);
                this.b = null;
            }
        }
        T t = (T) this.d.remove(this);
        if (t != null) {
            t.tryComplete();
        }
    }
}
