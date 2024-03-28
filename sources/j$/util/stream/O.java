package j$.util.stream;

import j$.util.function.Function;
import j$.util.function.ToLongFunction;
/* loaded from: classes2.dex */
class O extends d1 {
    public final /* synthetic */ int l = 1;
    final /* synthetic */ Object m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(U u, c cVar, f4 f4Var, int i, j$.util.function.h hVar) {
        super(cVar, f4Var, i);
        this.m = hVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public n3 F0(int i, n3 n3Var) {
        switch (this.l) {
            case 0:
                return new K(this, n3Var);
            case 1:
                return new G0(this, n3Var);
            case 2:
                return new a1(this, n3Var);
            case 3:
                return new a1(this, n3Var, (j$.lang.a) null);
            case 4:
                return new a1(this, n3Var, (j$.lang.b) null);
            case 5:
                return new a1(this, n3Var, (j$.lang.c) null);
            case 6:
                return new s(this, n3Var);
            default:
                return new Z2(this, n3Var);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(M0 m0, c cVar, f4 f4Var, int i, j$.util.function.n nVar) {
        super(cVar, f4Var, i);
        this.m = nVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(e1 e1Var, c cVar, f4 f4Var, int i, j$.util.function.q qVar) {
        super(cVar, f4Var, i);
        this.m = qVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(e1 e1Var, c cVar, f4 f4Var, int i, j$.util.function.r rVar) {
        super(cVar, f4Var, i);
        this.m = rVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(e1 e1Var, c cVar, f4 f4Var, int i, j$.util.function.t tVar) {
        super(cVar, f4Var, i);
        this.m = tVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(e1 e1Var, c cVar, f4 f4Var, int i, j$.wrappers.i0 i0Var) {
        super(cVar, f4Var, i);
        this.m = i0Var;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(f3 f3Var, c cVar, f4 f4Var, int i, Function function) {
        super(cVar, f4Var, i);
        this.m = function;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(f3 f3Var, c cVar, f4 f4Var, int i, ToLongFunction toLongFunction) {
        super(cVar, f4Var, i);
        this.m = toLongFunction;
    }
}
