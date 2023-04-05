package j$.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class Comparator$-EL {
    public static Comparator a(Comparator comparator, Comparator comparator2) {
        if (comparator instanceof e) {
            return ((f) ((e) comparator)).thenComparing(comparator2);
        }
        Objects.requireNonNull(comparator2);
        return new c(comparator, comparator2);
    }

    public static Comparator reversed(Comparator comparator) {
        if (comparator instanceof e) {
            Objects.requireNonNull((f) ((e) comparator));
            return Comparator$-CC.reverseOrder();
        }
        return Collections.reverseOrder(comparator);
    }
}
