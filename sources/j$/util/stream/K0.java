package j$.util.stream;

import j$.util.function.LongFunction;
import java.util.concurrent.CountedCompleter;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class K0 extends f {
    protected final u0 h;
    protected final LongFunction i;
    protected final j$.util.function.f j;

    K0(K0 k0, j$.util.Q q) {
        super(k0, q);
        this.h = k0.h;
        this.i = k0.i;
        this.j = k0.j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public K0(u0 u0Var, j$.util.Q q, LongFunction longFunction, l lVar) {
        super(u0Var, q);
        this.h = u0Var;
        this.i = longFunction;
        this.j = lVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public final Object a() {
        y0 y0Var = (y0) this.i.apply(this.h.I0(this.b));
        this.h.X0(this.b, y0Var);
        return y0Var.build();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public final f d(j$.util.Q q) {
        return new K0(this, q);
    }

    @Override // j$.util.stream.f, java.util.concurrent.CountedCompleter
    public final void onCompletion(CountedCompleter countedCompleter) {
        f fVar = this.d;
        if (!(fVar == null)) {
            e((D0) this.j.apply((D0) ((K0) fVar).b(), (D0) ((K0) this.e).b()));
        }
        super.onCompletion(countedCompleter);
    }
}
