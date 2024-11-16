package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
public final /* synthetic */ class g3 implements d2 {
    public final /* synthetic */ int a;
    public final /* synthetic */ j$.util.function.W b;

    public /* synthetic */ g3(j$.util.function.W w, int i) {
        this.a = i;
        this.b = w;
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
    public final void accept(long j) {
        switch (this.a) {
            case 0:
                ((L2) this.b).accept(j);
                break;
            default:
                this.b.accept(j);
                break;
        }
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public final /* bridge */ /* synthetic */ void r(Object obj) {
        switch (this.a) {
            case 0:
                j((Long) obj);
                break;
            default:
                j((Long) obj);
                break;
        }
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        switch (this.a) {
        }
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.function.W
    public final /* synthetic */ j$.util.function.W f(j$.util.function.W w) {
        switch (this.a) {
        }
        return j$.com.android.tools.r8.a.d(this, w);
    }

    @Override // j$.util.stream.d2
    public final /* synthetic */ void j(Long l) {
        switch (this.a) {
            case 0:
                t0.i(this, l);
                break;
            default:
                t0.i(this, l);
                break;
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

    @Override // j$.util.stream.e2
    public final /* synthetic */ boolean q() {
        switch (this.a) {
        }
        return false;
    }
}
