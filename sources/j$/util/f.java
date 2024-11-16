package j$.util;

import j$.util.function.Function;
import j$.util.function.v0;
import j$.util.function.x0;
import j$.util.function.z0;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* loaded from: classes2.dex */
final class f implements Comparator, e {
    public static final f INSTANCE;
    private static final /* synthetic */ f[] a;

    static {
        f fVar = new f();
        INSTANCE = fVar;
        a = new f[]{fVar};
    }

    private f() {
    }

    public static f valueOf(String str) {
        return (f) Enum.valueOf(f.class, str);
    }

    public static f[] values() {
        return (f[]) a.clone();
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        return ((Comparable) obj).compareTo((Comparable) obj2);
    }

    @Override // java.util.Comparator
    public final Comparator reversed() {
        return Comparator$-CC.reverseOrder();
    }

    @Override // java.util.Comparator
    public final Comparator thenComparing(Comparator comparator) {
        comparator.getClass();
        return new d(this, comparator, 0);
    }

    @Override // java.util.Comparator
    public final Comparator thenComparing(Function function) {
        return Comparator$-EL.a(this, Comparator$-CC.comparing(Function.VivifiedWrapper.convert(function)));
    }

    @Override // java.util.Comparator
    public final Comparator thenComparing(java.util.function.Function function, Comparator comparator) {
        j$.util.function.Function convert = Function.VivifiedWrapper.convert(function);
        convert.getClass();
        comparator.getClass();
        return Comparator$-EL.a(this, new d(comparator, convert, 1));
    }

    @Override // java.util.Comparator
    public final Comparator thenComparingDouble(ToDoubleFunction toDoubleFunction) {
        return Comparator$-EL.a(this, Comparator$-CC.comparingDouble(v0.a(toDoubleFunction)));
    }

    @Override // java.util.Comparator
    public final Comparator thenComparingInt(ToIntFunction toIntFunction) {
        return Comparator$-EL.a(this, Comparator$-CC.comparingInt(x0.a(toIntFunction)));
    }

    @Override // java.util.Comparator
    public final Comparator thenComparingLong(ToLongFunction toLongFunction) {
        return Comparator$-EL.a(this, Comparator$-CC.comparingLong(z0.a(toLongFunction)));
    }
}
