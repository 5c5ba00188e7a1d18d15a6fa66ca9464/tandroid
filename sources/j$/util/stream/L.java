package j$.util.stream;

import j$.util.function.Function;
import j$.util.function.ToDoubleFunction;
/* loaded from: classes2.dex */
class L extends T {
    public final /* synthetic */ int l = 4;
    final /* synthetic */ Object m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public L(U u, c cVar, f4 f4Var, int i, j$.util.function.f fVar) {
        super(cVar, f4Var, i);
        this.m = fVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public n3 F0(int i, n3 n3Var) {
        switch (this.l) {
            case 0:
                return new K(this, n3Var);
            case 1:
                return new K(this, n3Var, (j$.lang.a) null);
            case 2:
                return new K(this, n3Var, (j$.lang.b) null);
            case 3:
                return new K(this, n3Var, (j$.lang.c) null);
            case 4:
                return new G0(this, n3Var);
            case 5:
                return new a1(this, n3Var);
            case 6:
                return new Z2(this, n3Var);
            default:
                return new s(this, n3Var);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public L(U u, c cVar, f4 f4Var, int i, j$.util.function.g gVar) {
        super(cVar, f4Var, i);
        this.m = gVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public L(U u, c cVar, f4 f4Var, int i, j$.wrappers.D d) {
        super(cVar, f4Var, i);
        this.m = d;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public L(U u, c cVar, f4 f4Var, int i, j$.wrappers.J j) {
        super(cVar, f4Var, i);
        this.m = j;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public L(M0 m0, c cVar, f4 f4Var, int i, j$.wrappers.W w) {
        super(cVar, f4Var, i);
        this.m = w;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public L(e1 e1Var, c cVar, f4 f4Var, int i, j$.wrappers.k0 k0Var) {
        super(cVar, f4Var, i);
        this.m = k0Var;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public L(f3 f3Var, c cVar, f4 f4Var, int i, Function function) {
        super(cVar, f4Var, i);
        this.m = function;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public L(f3 f3Var, c cVar, f4 f4Var, int i, ToDoubleFunction toDoubleFunction) {
        super(cVar, f4Var, i);
        this.m = toDoubleFunction;
    }
}
