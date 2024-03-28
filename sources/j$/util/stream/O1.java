package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
class O1 extends f {
    protected final z2 h;
    protected final j$.util.function.r i;
    protected final j$.util.function.b j;

    O1(O1 o1, j$.util.t tVar) {
        super(o1, tVar);
        this.h = o1.h;
        this.i = o1.i;
        this.j = o1.j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public O1(z2 z2Var, j$.util.t tVar, j$.util.function.r rVar, j$.util.function.b bVar) {
        super(z2Var, tVar);
        this.h = z2Var;
        this.i = rVar;
        this.j = bVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public Object a() {
        t1 t1Var = (t1) this.i.apply(this.h.o0(this.b));
        this.h.s0(t1Var, this.b);
        return t1Var.a();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public f f(j$.util.t tVar) {
        return new O1(this, tVar);
    }

    @Override // j$.util.stream.f, java.util.concurrent.CountedCompleter
    public void onCompletion(CountedCompleter countedCompleter) {
        if (!d()) {
            g((B1) this.j.apply((B1) ((O1) this.d).b(), (B1) ((O1) this.e).b()));
        }
        this.b = null;
        this.e = null;
        this.d = null;
    }
}
