package j$.util;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
public interface Iterator<E> {

    /* loaded from: classes2.dex */
    public final /* synthetic */ class -CC {
        public static void $default$forEachRemaining(java.util.Iterator it, Consumer consumer) {
            Objects.requireNonNull(consumer);
            while (it.hasNext()) {
                consumer.accept(it.next());
            }
        }

        public static void a(java.util.Iterator it) {
            throw new UnsupportedOperationException("remove");
        }
    }

    void forEachRemaining(Consumer<? super E> consumer);

    boolean hasNext();

    E next();

    void remove();
}
