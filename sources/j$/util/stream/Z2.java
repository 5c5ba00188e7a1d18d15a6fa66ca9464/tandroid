package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
final class Z2 extends a3 implements Consumer {
    final Object[] b = new Object[128];

    Z2() {
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        int i = this.a;
        this.a = i + 1;
        this.b[i] = obj;
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }
}
