package j$.util.stream;

import java.util.concurrent.CountedCompleter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class P1 extends e {
    private final t0 h;

    P1(P1 p1, j$.util.Q q) {
        super(p1, q);
        this.h = p1.h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public P1(t0 t0Var, b bVar, j$.util.Q q) {
        super(bVar, q);
        this.h = t0Var;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.e
    public final Object a() {
        b bVar = this.a;
        N1 d0 = this.h.d0();
        bVar.D0(this.b, d0);
        return d0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.e
    public final e d(j$.util.Q q) {
        return new P1(this, q);
    }

    @Override // j$.util.stream.e, java.util.concurrent.CountedCompleter
    public final void onCompletion(CountedCompleter countedCompleter) {
        e eVar = this.d;
        if (eVar != null) {
            N1 n1 = (N1) ((P1) eVar).b();
            n1.h((N1) ((P1) this.e).b());
            e(n1);
        }
        super.onCompletion(countedCompleter);
    }
}
