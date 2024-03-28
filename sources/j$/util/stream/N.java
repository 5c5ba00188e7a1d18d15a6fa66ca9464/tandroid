package j$.util.stream;

import j$.util.function.Function;
import j$.util.function.ToIntFunction;
/* loaded from: classes2.dex */
class N extends L0 {
    public final /* synthetic */ int l = 1;
    final /* synthetic */ Object m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(U u, c cVar, f4 f4Var, int i, j$.wrappers.F f) {
        super(cVar, f4Var, i);
        this.m = f;
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
                return new G0(this, n3Var, (j$.lang.a) null);
            case 3:
                return new G0(this, n3Var, (j$.lang.b) null);
            case 4:
                return new G0(this, n3Var, (j$.lang.c) null);
            case 5:
                return new a1(this, n3Var);
            case 6:
                return new Z2(this, n3Var);
            default:
                return new s(this, n3Var);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(M0 m0, c cVar, f4 f4Var, int i, j$.util.function.l lVar) {
        super(cVar, f4Var, i);
        this.m = lVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(M0 m0, c cVar, f4 f4Var, int i, j$.util.function.m mVar) {
        super(cVar, f4Var, i);
        this.m = mVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(M0 m0, c cVar, f4 f4Var, int i, j$.wrappers.U u) {
        super(cVar, f4Var, i);
        this.m = u;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(M0 m0, c cVar, f4 f4Var, int i, j$.wrappers.a0 a0Var) {
        super(cVar, f4Var, i);
        this.m = a0Var;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(e1 e1Var, c cVar, f4 f4Var, int i, j$.wrappers.m0 m0Var) {
        super(cVar, f4Var, i);
        this.m = m0Var;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(f3 f3Var, c cVar, f4 f4Var, int i, Function function) {
        super(cVar, f4Var, i);
        this.m = function;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public N(f3 f3Var, c cVar, f4 f4Var, int i, ToIntFunction toIntFunction) {
        super(cVar, f4Var, i);
        this.m = toIntFunction;
    }
}
