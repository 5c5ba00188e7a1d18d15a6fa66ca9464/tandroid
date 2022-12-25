package j$.util;

import j$.wrappers.A0;
import j$.wrappers.C0;
import j$.wrappers.E0;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public enum f implements Comparator, e {
    INSTANCE;

    @Override // java.util.Comparator
    public int compare(Object obj, Object obj2) {
        return ((Comparable) obj).compareTo((Comparable) obj2);
    }

    @Override // java.util.Comparator
    public Comparator reversed() {
        return Comparator$-CC.reverseOrder();
    }

    @Override // java.util.Comparator
    public Comparator thenComparing(Comparator comparator) {
        Objects.requireNonNull(comparator);
        return new c(this, comparator);
    }

    @Override // java.util.Comparator
    public Comparator thenComparingDouble(ToDoubleFunction toDoubleFunction) {
        j$.util.function.z a2 = A0.a(toDoubleFunction);
        Objects.requireNonNull(a2);
        return a.H(this, new d(a2));
    }

    @Override // java.util.Comparator
    public Comparator thenComparingInt(ToIntFunction toIntFunction) {
        return a.H(this, Comparator$-CC.comparingInt(C0.a(toIntFunction)));
    }

    @Override // java.util.Comparator
    public Comparator thenComparingLong(ToLongFunction toLongFunction) {
        j$.util.function.A a2 = E0.a(toLongFunction);
        Objects.requireNonNull(a2);
        return a.H(this, new d(a2));
    }

    @Override // java.util.Comparator
    public Comparator thenComparing(Function function) {
        return a.H(this, Comparator$-CC.comparing(j$.wrappers.L.a(function)));
    }

    @Override // java.util.Comparator
    public Comparator thenComparing(Function function, Comparator comparator) {
        j$.util.function.Function a2 = j$.wrappers.L.a(function);
        Objects.requireNonNull(a2);
        Objects.requireNonNull(comparator);
        return a.H(this, new c(comparator, a2));
    }
}
