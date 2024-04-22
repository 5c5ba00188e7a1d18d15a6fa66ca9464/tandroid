package j$.util.stream;

import j$.util.function.LongFunction;
import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
class N1 extends f {
    protected final y2 h;
    protected final LongFunction i;
    protected final j$.util.function.b j;

    N1(N1 n1, j$.util.s sVar) {
        super(n1, sVar);
        this.h = n1.h;
        this.i = n1.i;
        this.j = n1.j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public N1(y2 y2Var, j$.util.s sVar, LongFunction longFunction, j$.util.function.b bVar) {
        super(y2Var, sVar);
        this.h = y2Var;
        this.i = longFunction;
        this.j = bVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public Object a() {
        s1 s1Var = (s1) this.i.apply(this.h.l0(this.b));
        this.h.p0(s1Var, this.b);
        return s1Var.a();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public f f(j$.util.s sVar) {
        return new N1(this, sVar);
    }

    @Override // j$.util.stream.f, java.util.concurrent.CountedCompleter
    public void onCompletion(CountedCompleter countedCompleter) {
        if (!d()) {
            g((A1) this.j.apply((A1) ((N1) this.d).b(), (A1) ((N1) this.e).b()));
        }
        this.b = null;
        this.e = null;
        this.d = null;
    }
}
