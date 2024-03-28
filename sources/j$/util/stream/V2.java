package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class V2 implements O4 {
    private final f4 a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public V2(f4 f4Var) {
        this.a = f4Var;
    }

    public abstract T2 a();

    @Override // j$.util.stream.O4
    public /* synthetic */ int b() {
        return 0;
    }

    @Override // j$.util.stream.O4
    public Object c(z2 z2Var, j$.util.t tVar) {
        return ((T2) new W2(this, z2Var, tVar).invoke()).get();
    }

    @Override // j$.util.stream.O4
    public Object d(z2 z2Var, j$.util.t tVar) {
        T2 a = a();
        c cVar = (c) z2Var;
        Objects.requireNonNull(a);
        cVar.l0(cVar.t0(a), tVar);
        return a.get();
    }
}
