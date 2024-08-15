package j$.util.function;

import j$.util.function.BiConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class a implements java.util.function.BiConsumer {
    public final /* synthetic */ BiConsumer a;

    private /* synthetic */ a(BiConsumer biConsumer) {
        this.a = biConsumer;
    }

    public static /* synthetic */ java.util.function.BiConsumer a(BiConsumer biConsumer) {
        if (biConsumer == null) {
            return null;
        }
        return biConsumer instanceof BiConsumer.VivifiedWrapper ? ((BiConsumer.VivifiedWrapper) biConsumer).a : new a(biConsumer);
    }

    @Override // java.util.function.BiConsumer
    public final /* synthetic */ void accept(Object obj, Object obj2) {
        this.a.accept(obj, obj2);
    }

    @Override // java.util.function.BiConsumer
    public final /* synthetic */ java.util.function.BiConsumer andThen(java.util.function.BiConsumer biConsumer) {
        return a(this.a.andThen(BiConsumer.VivifiedWrapper.convert(biConsumer)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        BiConsumer biConsumer = this.a;
        if (obj instanceof a) {
            obj = ((a) obj).a;
        }
        return biConsumer.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
