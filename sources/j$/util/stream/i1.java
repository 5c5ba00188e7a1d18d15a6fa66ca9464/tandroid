package j$.util.stream;

import java.util.Objects;
/* loaded from: classes2.dex */
class i1 extends j1 implements j3 {
    final /* synthetic */ k1 c;
    final /* synthetic */ j$.wrappers.D d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public i1(k1 k1Var, j$.wrappers.D d) {
        super(k1Var);
        this.c = k1Var;
        this.d = d;
    }

    @Override // j$.util.stream.j1, j$.util.stream.m3
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
        o1.a(this, d);
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return new j$.util.function.e(this, fVar);
    }
}
