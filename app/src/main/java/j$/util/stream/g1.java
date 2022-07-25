package j$.util.stream;
/* loaded from: classes2.dex */
class g1 extends j1 implements k3 {
    final /* synthetic */ k1 c;
    final /* synthetic */ j$.wrappers.V d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public g1(k1 k1Var, j$.wrappers.V v) {
        super(k1Var);
        this.c = k1Var;
        this.d = v;
    }

    @Override // j$.util.stream.j1, j$.util.stream.m3
    public void accept(int i) {
        boolean z;
        boolean z2;
        if (!this.a) {
            boolean b = this.d.b(i);
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
    public /* synthetic */ void accept(Integer num) {
        o1.b(this, num);
    }

    @Override // j$.util.function.l
    public j$.util.function.l l(j$.util.function.l lVar) {
        lVar.getClass();
        return new j$.util.function.k(this, lVar);
    }
}
