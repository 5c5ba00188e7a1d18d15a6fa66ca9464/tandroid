package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class Q1 extends f {
    private final u1 h;

    Q1(Q1 q1, j$.util.Q q) {
        super(q1, q);
        this.h = q1.h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Q1(u1 u1Var, u0 u0Var, j$.util.Q q) {
        super(u0Var, q);
        this.h = u1Var;
    }

    @Override // j$.util.stream.f
    protected final Object a() {
        u0 u0Var = this.a;
        O1 u = this.h.u();
        u0Var.X0(this.b, u);
        return u;
    }

    @Override // j$.util.stream.f
    protected final f d(j$.util.Q q) {
        return new Q1(this, q);
    }

    @Override // j$.util.stream.f, java.util.concurrent.CountedCompleter
    public final void onCompletion(CountedCompleter countedCompleter) {
        f fVar = this.d;
        if (!(fVar == null)) {
            O1 o1 = (O1) ((Q1) fVar).b();
            o1.k((O1) ((Q1) this.e).b());
            e(o1);
        }
        super.onCompletion(countedCompleter);
    }
}
