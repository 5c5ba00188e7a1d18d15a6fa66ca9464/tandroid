package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
/* loaded from: classes2.dex */
public class L extends d3 {
    public final /* synthetic */ int l = 1;
    final /* synthetic */ Object m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public L(T t, c cVar, e4 e4Var, int i, j$.util.function.g gVar) {
        super(cVar, e4Var, i);
        this.m = gVar;
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
                return new Y2(this, m3Var);
            default:
                return new Y2(this, m3Var, (j$.lang.a) null);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public L(L0 l0, c cVar, e4 e4Var, int i, j$.util.function.m mVar) {
        super(cVar, e4Var, i);
        this.m = mVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public L(d1 d1Var, c cVar, e4 e4Var, int i, j$.util.function.r rVar) {
        super(cVar, e4Var, i);
        this.m = rVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public L(e3 e3Var, c cVar, e4 e4Var, int i, Consumer consumer) {
        super(cVar, e4Var, i);
        this.m = consumer;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public L(e3 e3Var, c cVar, e4 e4Var, int i, Predicate predicate) {
        super(cVar, e4Var, i);
        this.m = predicate;
    }
}
