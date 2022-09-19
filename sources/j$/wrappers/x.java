package j$.wrappers;

import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class x implements Consumer {
    final /* synthetic */ j$.util.function.Consumer a;

    private /* synthetic */ x(j$.util.function.Consumer consumer) {
        this.a = consumer;
    }

    public static /* synthetic */ Consumer a(j$.util.function.Consumer consumer) {
        if (consumer == null) {
            return null;
        }
        return consumer instanceof w ? ((w) consumer).a : new x(consumer);
    }

    @Override // java.util.function.Consumer
    public /* synthetic */ void accept(Object obj) {
        this.a.accept(obj);
    }

    @Override // java.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return a(this.a.andThen(w.b(consumer)));
    }
}
