package j$.util.stream;

import java.util.Objects;
/* loaded from: classes2.dex */
class h1 extends k1 implements l3 {
    final /* synthetic */ l1 c;
    final /* synthetic */ j$.wrappers.U d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public h1(l1 l1Var, j$.wrappers.U u) {
        super(l1Var);
        this.c = l1Var;
        this.d = u;
    }

    @Override // j$.util.stream.k1, j$.util.stream.n3
    public void accept(int i) {
        boolean z;
        boolean z2;
        if (this.a) {
            return;
        }
        boolean b = this.d.b(i);
        z = this.c.a;
        if (b == z) {
            this.a = true;
            z2 = this.c.b;
            this.b = z2;
        }
    }

    @Override // j$.util.function.Consumer
    /* renamed from: b */
    public /* synthetic */ void accept(Integer num) {
        p1.b(this, num);
    }

    @Override // j$.util.function.l
    public j$.util.function.l l(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        return new j$.util.function.k(this, lVar);
    }
}
