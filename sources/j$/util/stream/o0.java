package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class o0 extends p0 {
    final Consumer b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o0(Consumer consumer, boolean z) {
        super(z);
        this.b = consumer;
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        this.b.accept(obj);
    }
}
