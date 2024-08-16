package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
/* loaded from: classes2.dex */
public interface Q {
    void a(Consumer consumer);

    int characteristics();

    long estimateSize();

    Comparator getComparator();

    long getExactSizeIfKnown();

    boolean hasCharacteristics(int i);

    boolean s(Consumer consumer);

    Q trySplit();
}
