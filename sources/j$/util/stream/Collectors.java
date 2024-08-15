package j$.util.stream;

import j$.util.function.Supplier;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
/* loaded from: classes2.dex */
public final class Collectors {
    static final Set a;

    static {
        i iVar = i.CONCURRENT;
        i iVar2 = i.UNORDERED;
        i iVar3 = i.IDENTITY_FINISH;
        Collections.unmodifiableSet(EnumSet.of(iVar, iVar2, iVar3));
        Collections.unmodifiableSet(EnumSet.of(iVar, iVar2));
        a = Collections.unmodifiableSet(EnumSet.of(iVar3));
        Collections.unmodifiableSet(EnumSet.of(iVar2, iVar3));
        Collections.emptySet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(double[] dArr, double d) {
        double d2 = d - dArr[1];
        double d3 = dArr[0];
        double d4 = d3 + d2;
        dArr[1] = (d4 - d3) - d2;
        dArr[0] = d4;
    }

    public static <T, C extends Collection<T>> Collector<T, ?, C> toCollection(Supplier<C> supplier) {
        return new m(supplier, new J0(9), new l(0), a);
    }
}
