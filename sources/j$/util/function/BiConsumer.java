package j$.util.function;

import java.util.Objects;
/* loaded from: classes2.dex */
public interface BiConsumer<T, U> {

    /* loaded from: classes2.dex */
    public final /* synthetic */ class -CC {
        public static BiConsumer $default$andThen(BiConsumer biConsumer, BiConsumer biConsumer2) {
            Objects.requireNonNull(biConsumer2);
            return new j$.util.concurrent.a(biConsumer, biConsumer2);
        }
    }

    void accept(T t, U u);

    BiConsumer<T, U> andThen(BiConsumer<? super T, ? super U> biConsumer);
}
