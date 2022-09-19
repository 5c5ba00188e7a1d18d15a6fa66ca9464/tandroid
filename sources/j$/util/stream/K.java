package j$.util.stream;

import j$.util.function.Function;
/* loaded from: classes2.dex */
class K extends S {
    public final /* synthetic */ int l = 4;
    final /* synthetic */ Object m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public K(T t, c cVar, e4 e4Var, int i, j$.util.function.f fVar) {
        super(cVar, e4Var, i);
        this.m = fVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public m3 H0(int i, m3 m3Var) {
        switch (this.l) {
            case 0:
                return new J(this, m3Var);
            case 1:
                return new J(this, m3Var, (j$.lang.a) null);
            case 2:
                return new J(this, m3Var, (j$.lang.b) null);
            case 3:
                return new J(this, m3Var, (j$.lang.c) null);
            case 4:
                return new F0(this, m3Var);
            case 5:
                return new Z0(this, m3Var);
            case 6:
                return new Y2(this, m3Var);
            default:
                return new r(this, m3Var);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public K(T t, c cVar, e4 e4Var, int i, j$.util.function.g gVar) {
        super(cVar, e4Var, i);
        this.m = gVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public K(T t, c cVar, e4 e4Var, int i, j$.wrappers.E e) {
        super(cVar, e4Var, i);
        this.m = e;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public K(T t, c cVar, e4 e4Var, int i, j$.wrappers.K k) {
        super(cVar, e4Var, i);
        this.m = k;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public K(L0 l0, c cVar, e4 e4Var, int i, j$.wrappers.X x) {
        super(cVar, e4Var, i);
        this.m = x;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public K(d1 d1Var, c cVar, e4 e4Var, int i, j$.wrappers.l0 l0Var) {
        super(cVar, e4Var, i);
        this.m = l0Var;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public K(e3 e3Var, c cVar, e4 e4Var, int i, Function function) {
        super(cVar, e4Var, i);
        this.m = function;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public K(e3 e3Var, c cVar, e4 e4Var, int i, j$.util.function.z zVar) {
        super(cVar, e4Var, i);
        this.m = zVar;
    }
}
