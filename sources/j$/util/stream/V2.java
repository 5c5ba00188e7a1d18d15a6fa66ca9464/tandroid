package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class V2 extends f {
    private final U2 h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public V2(U2 u2, y2 y2Var, j$.util.s sVar) {
        super(y2Var, sVar);
        this.h = u2;
    }

    V2(V2 v2, j$.util.s sVar) {
        super(v2, sVar);
        this.h = v2.h;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public Object a() {
        y2 y2Var = this.a;
        S2 a = this.h.a();
        y2Var.p0(a, this.b);
        return a;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public f f(j$.util.s sVar) {
        return new V2(this, sVar);
    }

    @Override // j$.util.stream.f, java.util.concurrent.CountedCompleter
    public void onCompletion(CountedCompleter countedCompleter) {
        if (!d()) {
            S2 s2 = (S2) ((V2) this.d).b();
            s2.h((S2) ((V2) this.e).b());
            g(s2);
        }
        this.b = null;
        this.e = null;
        this.d = null;
    }
}
