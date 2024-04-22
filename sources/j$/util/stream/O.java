package j$.util.stream;

import j$.util.function.Function;
import j$.util.function.LongFunction;
import j$.util.function.ToLongFunction;
/* loaded from: classes2.dex */
class O extends d1 {
    public final /* synthetic */ int l = 1;
    final /* synthetic */ Object m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(U u, c cVar, e4 e4Var, int i, j$.util.function.h hVar) {
        super(cVar, e4Var, i);
        this.m = hVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public m3 C0(int i, m3 m3Var) {
        switch (this.l) {
            case 0:
                return new K(this, m3Var);
            case 1:
                return new G0(this, m3Var);
            case 2:
                return new a1(this, m3Var);
            case 3:
                return new a1(this, m3Var, (j$.lang.a) null);
            case 4:
                return new a1(this, m3Var, (j$.lang.b) null);
            case 5:
                return new a1(this, m3Var, (j$.lang.c) null);
            case 6:
                return new s(this, m3Var);
            default:
                return new Y2(this, m3Var);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(M0 m0, c cVar, e4 e4Var, int i, j$.util.function.n nVar) {
        super(cVar, e4Var, i);
        this.m = nVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(e1 e1Var, c cVar, e4 e4Var, int i, j$.util.function.q qVar) {
        super(cVar, e4Var, i);
        this.m = qVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(e1 e1Var, c cVar, e4 e4Var, int i, LongFunction longFunction) {
        super(cVar, e4Var, i);
        this.m = longFunction;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(e1 e1Var, c cVar, e4 e4Var, int i, j$.util.function.s sVar) {
        super(cVar, e4Var, i);
        this.m = sVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(e1 e1Var, c cVar, e4 e4Var, int i, j$.wrappers.i0 i0Var) {
        super(cVar, e4Var, i);
        this.m = i0Var;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(e3 e3Var, c cVar, e4 e4Var, int i, Function function) {
        super(cVar, e4Var, i);
        this.m = function;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public O(e3 e3Var, c cVar, e4 e4Var, int i, ToLongFunction toLongFunction) {
        super(cVar, e4Var, i);
        this.m = toLongFunction;
    }
}
