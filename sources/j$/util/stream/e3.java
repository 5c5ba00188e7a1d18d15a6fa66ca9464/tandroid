package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class e3 implements c2 {
    public final /* synthetic */ int a;
    public final /* synthetic */ j$.util.function.F b;

    public /* synthetic */ e3(j$.util.function.F f, int i) {
        this.a = i;
        this.b = f;
    }

    private final /* synthetic */ void a(long j) {
    }

    private final /* synthetic */ void c(long j) {
    }

    private final /* synthetic */ void d() {
    }

    private final /* synthetic */ void e() {
    }

    @Override // j$.util.stream.e2, j$.util.function.n
    public final /* synthetic */ void accept(double d) {
        switch (this.a) {
            case 0:
                t0.b();
                throw null;
            default:
                t0.b();
                throw null;
        }
    }

    @Override // j$.util.stream.e2
    public final void accept(int i) {
        switch (this.a) {
            case 0:
                ((J2) this.b).accept(i);
                return;
            default:
                this.b.accept(i);
                return;
        }
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void accept(long j) {
        switch (this.a) {
            case 0:
                t0.l();
                throw null;
            default:
                t0.l();
                throw null;
        }
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        switch (this.a) {
            case 0:
                o((Integer) obj);
                return;
            default:
                o((Integer) obj);
                return;
        }
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        switch (this.a) {
            case 0:
                return Consumer.-CC.$default$andThen(this, consumer);
            default:
                return Consumer.-CC.$default$andThen(this, consumer);
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

    @Override // j$.util.stream.e2
    public final /* synthetic */ void m() {
        int i = this.a;
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void n(long j) {
        int i = this.a;
    }

    @Override // j$.util.stream.c2
    public final /* synthetic */ void o(Integer num) {
        switch (this.a) {
            case 0:
                t0.g(this, num);
                return;
            default:
                t0.g(this, num);
                return;
        }
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ boolean q() {
        switch (this.a) {
            case 0:
                return false;
            default:
                return false;
        }
    }
}
