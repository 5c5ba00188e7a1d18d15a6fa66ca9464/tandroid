package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class h3 implements e2 {
    public final /* synthetic */ int a;
    public final /* synthetic */ j$.util.function.h0 b;

    public /* synthetic */ h3(j$.util.function.h0 h0Var, int i) {
        this.a = i;
        this.b = h0Var;
    }

    @Override // j$.util.stream.f2, j$.util.stream.c2, j$.util.function.m
    public final /* synthetic */ void accept(double d) {
        switch (this.a) {
            case 0:
                u0.i0();
                throw null;
            default:
                u0.i0();
                throw null;
        }
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void accept(int i) {
        switch (this.a) {
            case 0:
                u0.p0();
                throw null;
            default:
                u0.p0();
                throw null;
        }
    }

    @Override // j$.util.stream.e2, j$.util.stream.f2
    public final void accept(long j) {
        int i = this.a;
        j$.util.function.h0 h0Var = this.b;
        switch (i) {
            case 0:
                ((M2) h0Var).accept(j);
                return;
            default:
                h0Var.accept(j);
                return;
        }
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        switch (this.a) {
            case 0:
                l((Long) obj);
                return;
            default:
                l((Long) obj);
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

    @Override // j$.util.stream.f2
    public final /* synthetic */ void end() {
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void f(long j) {
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ boolean h() {
        return false;
    }

    @Override // j$.util.function.h0
    public final j$.util.function.h0 i(j$.util.function.h0 h0Var) {
        switch (this.a) {
            case 0:
                h0Var.getClass();
                return new j$.util.function.e0(this, h0Var);
            default:
                h0Var.getClass();
                return new j$.util.function.e0(this, h0Var);
        }
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void l(Long l) {
        switch (this.a) {
            case 0:
                u0.n0(this, l);
                return;
            default:
                u0.n0(this, l);
                return;
        }
    }
}
