package j$.util;

import java.util.Objects;
/* loaded from: classes2.dex */
abstract class F {
    public int characteristics() {
        return 16448;
    }

    public long estimateSize() {
        return 0L;
    }

    public void forEachRemaining(Object obj) {
        Objects.requireNonNull(obj);
    }

    public boolean tryAdvance(Object obj) {
        Objects.requireNonNull(obj);
        return false;
    }

    public t trySplit() {
        return null;
    }
}
