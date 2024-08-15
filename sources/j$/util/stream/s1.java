package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class s1 extends t1 {
    public final /* synthetic */ int c;
    private final Object d;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ s1(C0 c0, Object obj) {
        this(c0, obj, 0);
        this.c = 0;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ s1(D0 d0, Object obj, int i) {
        super(d0);
        this.c = i;
        this.d = obj;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ s1(D0 d0, Object[] objArr) {
        this(d0, objArr, 1);
        this.c = 1;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public s1(s1 s1Var, C0 c0, int i) {
        super(s1Var, c0, i);
        this.c = 0;
        this.d = s1Var.d;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public s1(s1 s1Var, D0 d0, int i) {
        super(s1Var, d0, i);
        this.c = 1;
        this.d = (Object[]) s1Var.d;
    }

    @Override // j$.util.stream.t1
    final void a() {
        switch (this.c) {
            case 0:
                ((C0) this.a).c(this.d, this.b);
                return;
            default:
                this.a.e((Object[]) this.d, this.b);
                return;
        }
    }

    @Override // j$.util.stream.t1
    final s1 b(int i, int i2) {
        switch (this.c) {
            case 0:
                return new s1(this, ((C0) this.a).a(i), i2);
            default:
                return new s1(this, this.a.a(i), i2);
        }
    }
}
