package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
final class q3 extends r3 implements j$.util.K, j$.util.function.W {
    long e;

    q3(j$.util.K k, long j, long j2) {
        super(k, j, j2);
    }

    q3(j$.util.K k, q3 q3Var) {
        super(k, q3Var);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        j$.util.a.h(this, consumer);
    }

    @Override // j$.util.function.W
    public final void accept(long j) {
        this.e = j;
    }

    @Override // j$.util.function.W
    public final /* synthetic */ j$.util.function.W f(j$.util.function.W w) {
        return j$.com.android.tools.r8.a.d(this, w);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return j$.util.a.q(this, consumer);
    }

    @Override // j$.util.stream.u3
    protected final j$.util.Q u(j$.util.Q q) {
        return new q3((j$.util.K) q, this);
    }

    @Override // j$.util.stream.r3
    protected final void w(Object obj) {
        ((j$.util.function.W) obj).accept(this.e);
    }

    @Override // j$.util.stream.r3
    protected final Y2 x() {
        return new X2();
    }
}
