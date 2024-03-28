package j$.util.stream;

import java.util.Objects;
/* loaded from: classes2.dex */
class j1 extends k1 implements k3 {
    final /* synthetic */ l1 c;
    final /* synthetic */ j$.wrappers.D d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public j1(l1 l1Var, j$.wrappers.D d) {
        super(l1Var);
        this.c = l1Var;
        this.d = d;
    }

    @Override // j$.util.stream.k1, j$.util.stream.n3
    public void accept(double d) {
        boolean z;
        boolean z2;
        if (this.a) {
            return;
        }
        boolean b = this.d.b(d);
        z = this.c.a;
        if (b == z) {
            this.a = true;
            z2 = this.c.b;
            this.b = z2;
        }
    }

    @Override // j$.util.function.Consumer
    /* renamed from: b */
    public /* synthetic */ void accept(Double d) {
        p1.a(this, d);
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return new j$.util.function.e(this, fVar);
    }
}
