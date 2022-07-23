package j$.util.stream;
/* loaded from: classes2.dex */
public abstract class U2 implements N4 {
    private final e4 a;

    public U2(e4 e4Var) {
        this.a = e4Var;
    }

    public abstract S2 a();

    @Override // j$.util.stream.N4
    public /* synthetic */ int b() {
        return 0;
    }

    @Override // j$.util.stream.N4
    public Object c(y2 y2Var, j$.util.u uVar) {
        return ((S2) new V2(this, y2Var, uVar).invoke()).get();
    }

    @Override // j$.util.stream.N4
    public Object d(y2 y2Var, j$.util.u uVar) {
        S2 a = a();
        c cVar = (c) y2Var;
        a.getClass();
        cVar.n0(cVar.v0(a), uVar);
        return a.get();
    }
}
