package j$.util.stream;
/* loaded from: classes2.dex */
class w2 extends x2 {
    public final /* synthetic */ int c;
    private final Object d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public w2(A1 a1, Object obj, int i) {
        super(a1, i);
        this.c = 0;
        this.d = obj;
    }

    @Override // j$.util.stream.x2
    void a() {
        switch (this.c) {
            case 0:
                ((A1) this.a).d(this.d, this.b);
                return;
            default:
                this.a.i((Object[]) this.d, this.b);
                return;
        }
    }

    @Override // j$.util.stream.x2
    x2 b(int i, int i2) {
        switch (this.c) {
            case 0:
                return new w2(this, ((A1) this.a).b(i), i2);
            default:
                return new w2(this, this.a.b(i), i2);
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ w2(A1 a1, Object obj, int i, C1 c1) {
        this(a1, obj, i);
        this.c = 0;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public w2(B1 b1, Object[] objArr, int i, C1 c1) {
        super(b1, i);
        this.c = 1;
        this.c = 1;
        this.d = objArr;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public w2(w2 w2Var, A1 a1, int i) {
        super(w2Var, a1, i);
        this.c = 0;
        this.d = w2Var.d;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public w2(w2 w2Var, B1 b1, int i) {
        super(w2Var, b1, i);
        this.c = 1;
        this.d = (Object[]) w2Var.d;
    }
}
