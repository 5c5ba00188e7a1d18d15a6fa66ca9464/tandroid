package j$.util.function;

import java.util.function.DoubleToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class w implements y {
    public final /* synthetic */ DoubleToLongFunction a;

    private /* synthetic */ w(DoubleToLongFunction doubleToLongFunction) {
        this.a = doubleToLongFunction;
    }

    public static /* synthetic */ y a(DoubleToLongFunction doubleToLongFunction) {
        if (doubleToLongFunction == null) {
            return null;
        }
        return doubleToLongFunction instanceof x ? ((x) doubleToLongFunction).a : new w(doubleToLongFunction);
    }

    @Override // j$.util.function.y
    public final /* synthetic */ long applyAsLong(double d) {
        return this.a.applyAsLong(d);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof w) {
            obj = ((w) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
