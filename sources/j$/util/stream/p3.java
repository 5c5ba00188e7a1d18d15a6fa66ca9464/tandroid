package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
final class p3 extends r3 implements j$.util.H, j$.util.function.F {
    int e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p3(j$.util.H h, long j, long j2) {
        super(h, j, j2);
    }

    p3(j$.util.H h, p3 p3Var) {
        super(h, p3Var);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        j$.util.a.f(this, consumer);
    }

    @Override // j$.util.function.F
    public final void accept(int i) {
        this.e = i;
    }

    @Override // j$.util.function.F
    public final /* synthetic */ j$.util.function.F l(j$.util.function.F f) {
        return j$.com.android.tools.r8.a.c(this, f);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return j$.util.a.o(this, consumer);
    }

    @Override // j$.util.stream.u3
    protected final j$.util.Q u(j$.util.Q q) {
        return new p3((j$.util.H) q, this);
    }

    @Override // j$.util.stream.r3
    protected final void w(Object obj) {
        ((j$.util.function.F) obj).accept(this.e);
    }

    @Override // j$.util.stream.r3
    protected final Y2 x() {
        return new W2();
    }
}
