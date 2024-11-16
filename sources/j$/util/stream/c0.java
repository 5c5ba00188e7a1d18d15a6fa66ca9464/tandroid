package j$.util.stream;

/* loaded from: classes2.dex */
public final /* synthetic */ class c0 implements j$.util.function.W {
    public final /* synthetic */ int a;
    public final /* synthetic */ e2 b;

    public /* synthetic */ c0(int i, e2 e2Var) {
        this.a = i;
        this.b = e2Var;
    }

    @Override // j$.util.function.W
    public final void accept(long j) {
        switch (this.a) {
            case 0:
                this.b.accept(j);
                break;
            default:
                ((e0) this.b).a.accept(j);
                break;
        }
    }

    @Override // j$.util.function.W
    public final /* synthetic */ j$.util.function.W f(j$.util.function.W w) {
        switch (this.a) {
        }
        return j$.com.android.tools.r8.a.d(this, w);
    }
}
