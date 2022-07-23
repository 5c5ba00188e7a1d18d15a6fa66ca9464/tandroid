package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class n0 extends o0 {
    final Consumer b;

    public n0(Consumer consumer, boolean z) {
        super(z);
        this.b = consumer;
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        this.b.accept(obj);
    }
}
