package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
final class M1 implements N1, d2 {
    private boolean a;
    private long b;
    final /* synthetic */ j$.util.function.S c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public M1(j$.util.function.S s) {
        this.c = s;
    }

    @Override // j$.util.stream.e2, j$.util.function.n
    public final /* synthetic */ void accept(double d) {
        t0.b();
        throw null;
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void accept(int i) {
        t0.k();
        throw null;
    }

    @Override // j$.util.stream.e2
    public final void accept(long j) {
        if (this.a) {
            this.a = false;
        } else {
            j = this.c.applyAsLong(this.b, j);
        }
        this.b = j;
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public final /* bridge */ /* synthetic */ void r(Object obj) {
        j((Long) obj);
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.function.W
    public final /* synthetic */ j$.util.function.W f(j$.util.function.W w) {
        return j$.com.android.tools.r8.a.d(this, w);
    }

    @Override // j$.util.function.Supplier
    public final Object get() {
        return this.a ? j$.util.n.a() : j$.util.n.d(this.b);
    }

    @Override // j$.util.stream.N1
    public final void h(N1 n1) {
        M1 m1 = (M1) n1;
        if (m1.a) {
            return;
        }
        accept(m1.b);
    }

    @Override // j$.util.stream.d2
    public final /* synthetic */ void j(Long l) {
        t0.i(this, l);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void m() {
    }

    @Override // j$.util.stream.e2
    public final void n(long j) {
        this.a = true;
        this.b = 0L;
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ boolean q() {
        return false;
    }
}
