package j$.util.stream;

import j$.util.function.Function;
/* loaded from: classes2.dex */
public class N extends c1 {
    public final /* synthetic */ int l = 1;
    final /* synthetic */ Object m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(T t, c cVar, e4 e4Var, int i, j$.util.function.h hVar) {
        super(cVar, e4Var, i);
        this.m = hVar;
    }

    @Override // j$.util.stream.c
    public m3 H0(int i, m3 m3Var) {
        switch (this.l) {
            case 0:
                return new J(this, m3Var);
            case 1:
                return new F0(this, m3Var);
            case 2:
                return new Z0(this, m3Var);
            case 3:
                return new Z0(this, m3Var, (j$.lang.a) null);
            case 4:
                return new Z0(this, m3Var, (j$.lang.b) null);
            case 5:
                return new Z0(this, m3Var, (j$.lang.c) null);
            case 6:
                return new r(this, m3Var);
            default:
                return new Y2(this, m3Var);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(L0 l0, c cVar, e4 e4Var, int i, j$.util.function.n nVar) {
        super(cVar, e4Var, i);
        this.m = nVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(d1 d1Var, c cVar, e4 e4Var, int i, j$.util.function.q qVar) {
        super(cVar, e4Var, i);
        this.m = qVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(d1 d1Var, c cVar, e4 e4Var, int i, j$.util.function.r rVar) {
        super(cVar, e4Var, i);
        this.m = rVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(d1 d1Var, c cVar, e4 e4Var, int i, j$.util.function.t tVar) {
        super(cVar, e4Var, i);
        this.m = tVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(d1 d1Var, c cVar, e4 e4Var, int i, j$.wrappers.j0 j0Var) {
        super(cVar, e4Var, i);
        this.m = j0Var;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(e3 e3Var, c cVar, e4 e4Var, int i, Function function) {
        super(cVar, e4Var, i);
        this.m = function;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(e3 e3Var, c cVar, e4 e4Var, int i, j$.util.function.A a) {
        super(cVar, e4Var, i);
        this.m = a;
    }
}
