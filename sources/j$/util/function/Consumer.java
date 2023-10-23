package j$.util.function;

import java.util.Objects;
/* loaded from: classes2.dex */
public interface Consumer<T> {

    /* loaded from: classes2.dex */
    public final /* synthetic */ class -CC {
        public static Consumer $default$andThen(Consumer consumer, Consumer consumer2) {
            Objects.requireNonNull(consumer2);
            return new j$.util.concurrent.a(consumer, consumer2);
        }
    }

    void accept(T t);

    Consumer<T> andThen(Consumer<? super T> consumer);
}
