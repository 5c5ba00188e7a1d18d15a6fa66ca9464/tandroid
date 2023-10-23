package j$.wrappers;

import j$.util.function.BiConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class q implements BiConsumer {
    final /* synthetic */ java.util.function.BiConsumer a;

    private /* synthetic */ q(java.util.function.BiConsumer biConsumer) {
        this.a = biConsumer;
    }

    public static /* synthetic */ BiConsumer a(java.util.function.BiConsumer biConsumer) {
        if (biConsumer == null) {
            return null;
        }
        return biConsumer instanceof r ? ((r) biConsumer).a : new q(biConsumer);
    }

    @Override // j$.util.function.BiConsumer
    public /* synthetic */ void accept(Object obj, Object obj2) {
        this.a.accept(obj, obj2);
    }

    @Override // j$.util.function.BiConsumer
    public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
        return a(this.a.andThen(r.a(biConsumer)));
    }
}
