package j$.util.stream;

import j$.util.function.LongFunction;
import java.util.concurrent.CountedCompleter;

/* loaded from: classes2.dex */
class L0 extends e {
    protected final b h;
    protected final LongFunction i;
    protected final j$.util.function.f j;

    L0(L0 l0, j$.util.Q q) {
        super(l0, q);
        this.h = l0.h;
        this.i = l0.i;
        this.j = l0.j;
    }

    L0(b bVar, j$.util.Q q, LongFunction longFunction, j$.util.function.f fVar) {
        super(bVar, q);
        this.h = bVar;
        this.i = longFunction;
        this.j = fVar;
    }

    @Override // j$.util.stream.e
    protected final Object a() {
        x0 x0Var = (x0) this.i.apply(this.h.o0(this.b));
        this.h.D0(this.b, x0Var);
        return x0Var.b();
    }

    @Override // j$.util.stream.e
    protected final e d(j$.util.Q q) {
        return new L0(this, q);
    }

    @Override // j$.util.stream.e, java.util.concurrent.CountedCompleter
    public final void onCompletion(CountedCompleter countedCompleter) {
        e eVar = this.d;
        if (eVar != null) {
            e((F0) this.j.apply((F0) ((L0) eVar).b(), (F0) ((L0) this.e).b()));
        }
        super.onCompletion(countedCompleter);
    }
}
