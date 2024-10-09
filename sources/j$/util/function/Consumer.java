package j$.util.function;

/* loaded from: classes2.dex */
public interface Consumer<T> {

    /* loaded from: classes2.dex */
    public final /* synthetic */ class -CC {
        public static Consumer $default$andThen(Consumer consumer, Consumer consumer2) {
            consumer2.getClass();
            return new j$.util.concurrent.s(3, consumer, consumer2);
        }
    }

    /* loaded from: classes2.dex */
    public final /* synthetic */ class Wrapper implements java.util.function.Consumer {
        private /* synthetic */ Wrapper() {
        }

        public static /* synthetic */ java.util.function.Consumer convert(Consumer consumer) {
            if (consumer == null) {
                return null;
            }
            return consumer instanceof g ? ((g) consumer).a : new Wrapper();
        }

        @Override // java.util.function.Consumer
        public final /* synthetic */ void accept(Object obj) {
            Consumer.this.accept(obj);
        }

        @Override // java.util.function.Consumer
        public final /* synthetic */ java.util.function.Consumer andThen(java.util.function.Consumer consumer) {
            return convert(Consumer.this.andThen(g.a(consumer)));
        }

        public final /* synthetic */ boolean equals(Object obj) {
            Consumer consumer = Consumer.this;
            if (obj instanceof Wrapper) {
                obj = Consumer.this;
            }
            return consumer.equals(obj);
        }

        public final /* synthetic */ int hashCode() {
            return Consumer.this.hashCode();
        }
    }

    void accept(T t);

    Consumer<T> andThen(Consumer<? super T> consumer);
}
