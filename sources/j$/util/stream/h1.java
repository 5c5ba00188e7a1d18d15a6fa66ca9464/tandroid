package j$.util.stream;

import java.util.Objects;
/* loaded from: classes2.dex */
class h1 extends j1 implements l3 {
    final /* synthetic */ k1 c;
    final /* synthetic */ j$.wrappers.i0 d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public h1(k1 k1Var, j$.wrappers.i0 i0Var) {
        super(k1Var);
        this.c = k1Var;
        this.d = i0Var;
    }

    @Override // j$.util.stream.j1, j$.util.stream.m3, j$.util.stream.l3, j$.util.function.q
    public void accept(long j) {
        boolean z;
        boolean z2;
        if (!this.a) {
            boolean b = this.d.b(j);
            z = this.c.a;
            if (b != z) {
                return;
            }
            this.a = true;
            z2 = this.c.b;
            this.b = z2;
        }
    }

    @Override // j$.util.function.Consumer
    /* renamed from: b */
    public /* synthetic */ void accept(Long l) {
        o1.c(this, l);
    }

    @Override // j$.util.function.q
    public j$.util.function.q f(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        return new j$.util.function.p(this, qVar);
    }
}
