package j$.util.function;
/* loaded from: classes2.dex */
public interface BiConsumer<T, U> {

    /* loaded from: classes2.dex */
    public final /* synthetic */ class -CC<T, U> {
        public static BiConsumer $default$andThen(BiConsumer biConsumer, BiConsumer biConsumer2) {
            biConsumer2.getClass();
            return new j$.util.concurrent.u(1, biConsumer, biConsumer2);
        }
    }

    /* loaded from: classes2.dex */
    public final /* synthetic */ class VivifiedWrapper implements BiConsumer {
        public final /* synthetic */ java.util.function.BiConsumer a;

        private /* synthetic */ VivifiedWrapper(java.util.function.BiConsumer biConsumer) {
            this.a = biConsumer;
        }

        public static /* synthetic */ BiConsumer convert(java.util.function.BiConsumer biConsumer) {
            if (biConsumer == null) {
                return null;
            }
            return biConsumer instanceof a ? ((a) biConsumer).a : new VivifiedWrapper(biConsumer);
        }

        @Override // j$.util.function.BiConsumer
        public final /* synthetic */ void accept(Object obj, Object obj2) {
            this.a.accept(obj, obj2);
        }

        @Override // j$.util.function.BiConsumer
        public final /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
            return convert(this.a.andThen(a.a(biConsumer)));
        }

        public final /* synthetic */ boolean equals(Object obj) {
            if (obj instanceof VivifiedWrapper) {
                obj = ((VivifiedWrapper) obj).a;
            }
            return this.a.equals(obj);
        }

        public final /* synthetic */ int hashCode() {
            return this.a.hashCode();
        }
    }

    void accept(T t, U u);

    BiConsumer<T, U> andThen(BiConsumer<? super T, ? super U> biConsumer);
}
