package j$.util.function;

import java.util.function.DoubleToLongFunction;

/* loaded from: classes2.dex */
public final /* synthetic */ class t implements v {
    public final /* synthetic */ DoubleToLongFunction a;

    private /* synthetic */ t(DoubleToLongFunction doubleToLongFunction) {
        this.a = doubleToLongFunction;
    }

    public static /* synthetic */ v a(DoubleToLongFunction doubleToLongFunction) {
        if (doubleToLongFunction == null) {
            return null;
        }
        return doubleToLongFunction instanceof u ? ((u) doubleToLongFunction).a : new t(doubleToLongFunction);
    }

    @Override // j$.util.function.v
    public final /* synthetic */ long applyAsLong(double d) {
        return this.a.applyAsLong(d);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        DoubleToLongFunction doubleToLongFunction = this.a;
        if (obj instanceof t) {
            obj = ((t) obj).a;
        }
        return doubleToLongFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
