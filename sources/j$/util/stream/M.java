package j$.util.stream;

import j$.util.function.Function;
import j$.util.function.ToIntFunction;
/* loaded from: classes2.dex */
class M extends K0 {
    public final /* synthetic */ int l = 1;
    final /* synthetic */ Object m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public M(T t, c cVar, e4 e4Var, int i, j$.wrappers.F f) {
        super(cVar, e4Var, i);
        this.m = f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public m3 H0(int i, m3 m3Var) {
        switch (this.l) {
            case 0:
                return new J(this, m3Var);
            case 1:
                return new F0(this, m3Var);
            case 2:
                return new F0(this, m3Var, (j$.lang.a) null);
            case 3:
                return new F0(this, m3Var, (j$.lang.b) null);
            case 4:
                return new F0(this, m3Var, (j$.lang.c) null);
            case 5:
                return new Z0(this, m3Var);
            case 6:
                return new Y2(this, m3Var);
            default:
                return new r(this, m3Var);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public M(L0 l0, c cVar, e4 e4Var, int i, j$.util.function.l lVar) {
        super(cVar, e4Var, i);
        this.m = lVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public M(L0 l0, c cVar, e4 e4Var, int i, j$.util.function.m mVar) {
        super(cVar, e4Var, i);
        this.m = mVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public M(L0 l0, c cVar, e4 e4Var, int i, j$.wrappers.U u) {
        super(cVar, e4Var, i);
        this.m = u;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public M(L0 l0, c cVar, e4 e4Var, int i, j$.wrappers.a0 a0Var) {
        super(cVar, e4Var, i);
        this.m = a0Var;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public M(d1 d1Var, c cVar, e4 e4Var, int i, j$.wrappers.m0 m0Var) {
        super(cVar, e4Var, i);
        this.m = m0Var;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public M(e3 e3Var, c cVar, e4 e4Var, int i, Function function) {
        super(cVar, e4Var, i);
        this.m = function;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public M(e3 e3Var, c cVar, e4 e4Var, int i, ToIntFunction toIntFunction) {
        super(cVar, e4Var, i);
        this.m = toIntFunction;
    }
}
