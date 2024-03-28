package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class L4 implements n3 {
    public final /* synthetic */ int a = 0;
    public final /* synthetic */ Object b;

    @Override // j$.util.stream.n3
    public /* synthetic */ void accept(double d) {
        switch (this.a) {
            case 0:
                p1.f(this);
                throw null;
            default:
                p1.f(this);
                throw null;
        }
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        switch (this.a) {
            case 0:
                return Consumer.-CC.$default$andThen(this, consumer);
            default:
                return Consumer.-CC.$default$andThen(this, consumer);
        }
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void m() {
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void n(long j) {
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ boolean o() {
        return false;
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void accept(int i) {
        switch (this.a) {
            case 0:
                p1.d(this);
                throw null;
            default:
                p1.d(this);
                throw null;
        }
    }

    @Override // j$.util.stream.n3, j$.util.stream.m3, j$.util.function.q
    public /* synthetic */ void accept(long j) {
        switch (this.a) {
            case 0:
                p1.e(this);
                throw null;
            default:
                p1.e(this);
                throw null;
        }
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.a) {
            case 0:
                ((Consumer) this.b).accept(obj);
                return;
            default:
                ((b4) this.b).accept(obj);
                return;
        }
    }
}
