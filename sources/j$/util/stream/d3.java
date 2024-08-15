package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class d3 implements c2 {
    public final /* synthetic */ int a;
    public final /* synthetic */ j$.util.function.m b;

    public /* synthetic */ d3(j$.util.function.m mVar, int i) {
        this.a = i;
        this.b = mVar;
    }

    @Override // j$.util.stream.c2, j$.util.function.m
    public final void accept(double d) {
        int i = this.a;
        j$.util.function.m mVar = this.b;
        switch (i) {
            case 0:
                ((I2) mVar).accept(d);
                return;
            default:
                mVar.accept(d);
                return;
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

    @Override // j$.util.stream.f2
    public final /* synthetic */ void accept(long j) {
        switch (this.a) {
            case 0:
                u0.q0();
                throw null;
            default:
                u0.q0();
                throw null;
        }
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        switch (this.a) {
            case 0:
                p((Double) obj);
                return;
            default:
                p((Double) obj);
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

    @Override // j$.util.function.m
    public final j$.util.function.m m(j$.util.function.m mVar) {
        switch (this.a) {
            case 0:
                mVar.getClass();
                return new j$.util.function.j(this, mVar);
            default:
                mVar.getClass();
                return new j$.util.function.j(this, mVar);
        }
    }

    @Override // j$.util.stream.c2
    public final /* synthetic */ void p(Double d) {
        switch (this.a) {
            case 0:
                u0.j0(this, d);
                return;
            default:
                u0.j0(this, d);
                return;
        }
    }
}
