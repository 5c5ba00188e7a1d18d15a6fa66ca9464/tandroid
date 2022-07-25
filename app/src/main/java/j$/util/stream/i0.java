package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
abstract class i0 implements O4 {
    boolean a;
    Object b;

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(double d) {
        o1.f(this);
        throw null;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(int i) {
        o1.d(this);
        throw null;
    }

    @Override // j$.util.stream.m3, j$.util.stream.l3, j$.util.function.q
    public /* synthetic */ void accept(long j) {
        o1.e(this);
        throw null;
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        if (!this.a) {
            this.a = true;
            this.b = obj;
        }
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return consumer.getClass();
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void m() {
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void n(long j) {
    }

    @Override // j$.util.stream.m3
    public boolean o() {
        return this.a;
    }
}
