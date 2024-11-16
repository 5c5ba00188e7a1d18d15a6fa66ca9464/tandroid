package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
public final /* synthetic */ class c3 implements b2 {
    public final /* synthetic */ int a;
    public final /* synthetic */ j$.util.function.n b;

    public /* synthetic */ c3(j$.util.function.n nVar, int i) {
        this.a = i;
        this.b = nVar;
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
    public final void accept(double d) {
        switch (this.a) {
            case 0:
                ((H2) this.b).accept(d);
                break;
            default:
                this.b.accept(d);
                break;
        }
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void accept(int i) {
        switch (this.a) {
            case 0:
                t0.k();
                throw null;
            default:
                t0.k();
                throw null;
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
    /* renamed from: accept */
    public final /* bridge */ /* synthetic */ void r(Object obj) {
        switch (this.a) {
            case 0:
                r((Double) obj);
                break;
            default:
                r((Double) obj);
                break;
        }
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        switch (this.a) {
        }
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.function.n
    public final /* synthetic */ j$.util.function.n k(j$.util.function.n nVar) {
        switch (this.a) {
        }
        return j$.com.android.tools.r8.a.b(this, nVar);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void m() {
        int i = this.a;
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void n(long j) {
        int i = this.a;
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ boolean q() {
        switch (this.a) {
        }
        return false;
    }

    @Override // j$.util.stream.b2
    public final /* synthetic */ void r(Double d) {
        switch (this.a) {
            case 0:
                t0.e(this, d);
                break;
            default:
                t0.e(this, d);
                break;
        }
    }
}
