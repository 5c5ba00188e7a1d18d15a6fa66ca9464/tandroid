package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class W2 extends f {
    private final V2 h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public W2(V2 v2, z2 z2Var, j$.util.t tVar) {
        super(z2Var, tVar);
        this.h = v2;
    }

    W2(W2 w2, j$.util.t tVar) {
        super(w2, tVar);
        this.h = w2.h;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public Object a() {
        z2 z2Var = this.a;
        T2 a = this.h.a();
        z2Var.s0(a, this.b);
        return a;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public f f(j$.util.t tVar) {
        return new W2(this, tVar);
    }

    @Override // j$.util.stream.f, java.util.concurrent.CountedCompleter
    public void onCompletion(CountedCompleter countedCompleter) {
        if (!d()) {
            T2 t2 = (T2) ((W2) this.d).b();
            t2.h((T2) ((W2) this.e).b());
            g(t2);
        }
        this.b = null;
        this.e = null;
        this.d = null;
    }
}
