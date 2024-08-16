package j$.util.stream;
/* loaded from: classes2.dex */
public final /* synthetic */ class U implements j$.util.function.F {
    public final /* synthetic */ int a;
    public final /* synthetic */ e2 b;

    public /* synthetic */ U(int i, e2 e2Var) {
        this.a = i;
        this.b = e2Var;
    }

    @Override // j$.util.function.F
    public final void accept(int i) {
        switch (this.a) {
            case 0:
                this.b.accept(i);
                return;
            default:
                ((W) this.b).a.accept(i);
                return;
        }
    }

    @Override // j$.util.function.F
    public final /* synthetic */ j$.util.function.F l(j$.util.function.F f) {
        switch (this.a) {
            case 0:
                return j$.com.android.tools.r8.a.c(this, f);
            default:
                return j$.com.android.tools.r8.a.c(this, f);
        }
    }
}
