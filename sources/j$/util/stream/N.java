package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class N extends Q implements c2 {
    final j$.util.function.F b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public N(j$.util.function.F f, boolean z) {
        super(z);
        this.b = f;
    }

    @Override // j$.util.stream.Q, j$.util.stream.e2
    public final void accept(int i) {
        this.b.accept(i);
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        o((Integer) obj);
    }

    @Override // j$.util.function.F
    public final /* synthetic */ j$.util.function.F l(j$.util.function.F f) {
        return j$.com.android.tools.r8.a.c(this, f);
    }

    @Override // j$.util.stream.c2
    public final /* synthetic */ void o(Integer num) {
        t0.g(this, num);
    }
}
