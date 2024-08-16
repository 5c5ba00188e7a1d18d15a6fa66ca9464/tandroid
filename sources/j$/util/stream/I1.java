package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class I1 implements N1, c2 {
    private boolean a;
    private int b;
    final /* synthetic */ j$.util.function.B c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public I1(j$.util.function.B b) {
        this.c = b;
    }

    @Override // j$.util.stream.e2, j$.util.function.n
    public final /* synthetic */ void accept(double d) {
        t0.b();
        throw null;
    }

    @Override // j$.util.stream.e2
    public final void accept(int i) {
        if (this.a) {
            this.a = false;
        } else {
            i = this.c.applyAsInt(this.b, i);
        }
        this.b = i;
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void accept(long j) {
        t0.l();
        throw null;
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        o((Integer) obj);
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.function.Supplier
    public final Object get() {
        return this.a ? j$.util.m.a() : j$.util.m.d(this.b);
    }

    @Override // j$.util.stream.N1
    public final void h(N1 n1) {
        I1 i1 = (I1) n1;
        if (i1.a) {
            return;
        }
        accept(i1.b);
    }

    @Override // j$.util.function.F
    public final /* synthetic */ j$.util.function.F l(j$.util.function.F f) {
        return j$.com.android.tools.r8.a.c(this, f);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void m() {
    }

    @Override // j$.util.stream.e2
    public final void n(long j) {
        this.a = true;
        this.b = 0;
    }

    @Override // j$.util.stream.c2
    public final /* synthetic */ void o(Integer num) {
        t0.g(this, num);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ boolean q() {
        return false;
    }
}
