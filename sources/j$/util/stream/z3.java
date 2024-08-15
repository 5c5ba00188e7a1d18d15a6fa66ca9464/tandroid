package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class z3 implements f2 {
    public final /* synthetic */ int a;
    public final /* synthetic */ Consumer b;

    public /* synthetic */ z3(Consumer consumer, int i) {
        this.a = i;
        this.b = consumer;
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
    public final void accept(Object obj) {
        int i = this.a;
        Consumer consumer = this.b;
        switch (i) {
            case 0:
                ((P2) consumer).accept(obj);
                return;
            default:
                consumer.accept(obj);
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
}
