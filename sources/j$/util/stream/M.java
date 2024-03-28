package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
/* loaded from: classes2.dex */
class M extends e3 {
    public final /* synthetic */ int l = 1;
    final /* synthetic */ Object m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public M(U u, c cVar, f4 f4Var, int i, j$.util.function.g gVar) {
        super(cVar, f4Var, i);
        this.m = gVar;
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
                return new Z2(this, n3Var);
            default:
                return new Z2(this, n3Var, (j$.lang.a) null);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public M(M0 m0, c cVar, f4 f4Var, int i, j$.util.function.m mVar) {
        super(cVar, f4Var, i);
        this.m = mVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public M(e1 e1Var, c cVar, f4 f4Var, int i, j$.util.function.r rVar) {
        super(cVar, f4Var, i);
        this.m = rVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public M(f3 f3Var, c cVar, f4 f4Var, int i, Consumer consumer) {
        super(cVar, f4Var, i);
        this.m = consumer;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public M(f3 f3Var, c cVar, f4 f4Var, int i, Predicate predicate) {
        super(cVar, f4Var, i);
        this.m = predicate;
    }
}
