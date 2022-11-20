package j$.util;

import j$.util.function.Function;
import j$.util.function.ToIntFunction;
import java.io.Serializable;
import java.util.Comparator;
/* loaded from: classes2.dex */
public final /* synthetic */ class d implements Comparator, Serializable {
    public final /* synthetic */ int a = 3;
    public final /* synthetic */ Object b;

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        switch (this.a) {
            case 0:
                Function function = (Function) this.b;
                return ((Comparable) function.apply(obj)).compareTo(function.apply(obj2));
            case 1:
                j$.util.function.z zVar = (j$.util.function.z) this.b;
                return Double.compare(zVar.applyAsDouble(obj), zVar.applyAsDouble(obj2));
            case 2:
                ToIntFunction toIntFunction = (ToIntFunction) this.b;
                return Integer.compare(toIntFunction.applyAsInt(obj), toIntFunction.applyAsInt(obj2));
            default:
                j$.util.function.A a = (j$.util.function.A) this.b;
                return Long.compare(a.applyAsLong(obj), a.applyAsLong(obj2));
        }
    }
}
