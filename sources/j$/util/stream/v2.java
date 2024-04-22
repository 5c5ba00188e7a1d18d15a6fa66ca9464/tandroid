package j$.util.stream;
/* loaded from: classes2.dex */
class v2 extends w2 {
    public final /* synthetic */ int c;
    private final Object d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public v2(z1 z1Var, Object obj, int i) {
        super(z1Var, i);
        this.c = 0;
        this.d = obj;
    }

    @Override // j$.util.stream.w2
    void a() {
        switch (this.c) {
            case 0:
                ((z1) this.a).d(this.d, this.b);
                return;
            default:
                this.a.i((Object[]) this.d, this.b);
                return;
        }
    }

    @Override // j$.util.stream.w2
    w2 b(int i, int i2) {
        switch (this.c) {
            case 0:
                return new v2(this, ((z1) this.a).b(i), i2);
            default:
                return new v2(this, this.a.b(i), i2);
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ v2(z1 z1Var, Object obj, int i, B1 b1) {
        this(z1Var, obj, i);
        this.c = 0;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public v2(A1 a1, Object[] objArr, int i, B1 b1) {
        super(a1, i);
        this.c = 1;
        this.c = 1;
        this.d = objArr;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public v2(v2 v2Var, z1 z1Var, int i) {
        super(v2Var, z1Var, i);
        this.c = 0;
        this.d = v2Var.d;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public v2(v2 v2Var, A1 a1, int i) {
        super(v2Var, a1, i);
        this.c = 1;
        this.d = (Object[]) v2Var.d;
    }
}
