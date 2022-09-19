package j$.wrappers;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class w implements Consumer {
    final /* synthetic */ java.util.function.Consumer a;

    private /* synthetic */ w(java.util.function.Consumer consumer) {
        this.a = consumer;
    }

    public static /* synthetic */ Consumer b(java.util.function.Consumer consumer) {
        if (consumer == null) {
            return null;
        }
        return consumer instanceof x ? ((x) consumer).a : new w(consumer);
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ void accept(Object obj) {
        this.a.accept(obj);
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return b(this.a.andThen(x.a(consumer)));
    }
}
