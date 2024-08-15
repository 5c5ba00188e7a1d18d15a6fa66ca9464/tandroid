package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class f3 implements d2 {
    public final /* synthetic */ int a;
    public final /* synthetic */ j$.util.function.K b;

    public /* synthetic */ f3(j$.util.function.K k, int i) {
        this.a = i;
        this.b = k;
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

    @Override // j$.util.stream.d2, j$.util.stream.f2
    public final void accept(int i) {
        int i2 = this.a;
        j$.util.function.K k = this.b;
        switch (i2) {
            case 0:
                ((K2) k).accept(i);
                return;
            default:
                k.accept(i);
                return;
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
                g((Integer) obj);
                return;
            default:
                g((Integer) obj);
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

    @Override // j$.util.stream.d2
    public final /* synthetic */ void g(Integer num) {
        switch (this.a) {
            case 0:
                u0.l0(this, num);
                return;
            default:
                u0.l0(this, num);
                return;
        }
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ boolean h() {
        return false;
    }

    @Override // j$.util.function.K
    public final j$.util.function.K n(j$.util.function.K k) {
        switch (this.a) {
            case 0:
                k.getClass();
                return new j$.util.function.H(this, k);
            default:
                k.getClass();
                return new j$.util.function.H(this, k);
        }
    }
}
