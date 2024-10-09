package j$.util.function;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
public final /* synthetic */ class g implements Consumer {
    public final /* synthetic */ java.util.function.Consumer a;

    private /* synthetic */ g(java.util.function.Consumer consumer) {
        this.a = consumer;
    }

    public static /* synthetic */ Consumer a(java.util.function.Consumer consumer) {
        if (consumer == null) {
            return null;
        }
        return consumer instanceof Consumer.Wrapper ? Consumer.this : new g(consumer);
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ void accept(Object obj) {
        this.a.accept(obj);
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return a(this.a.andThen(Consumer.Wrapper.convert(consumer)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        java.util.function.Consumer consumer = this.a;
        if (obj instanceof g) {
            obj = ((g) obj).a;
        }
        return consumer.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
