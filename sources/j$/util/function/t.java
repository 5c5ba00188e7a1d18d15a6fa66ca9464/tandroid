package j$.util.function;

import java.util.function.DoubleToIntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class t implements v {
    public final /* synthetic */ DoubleToIntFunction a;

    private /* synthetic */ t(DoubleToIntFunction doubleToIntFunction) {
        this.a = doubleToIntFunction;
    }

    public static /* synthetic */ v b(DoubleToIntFunction doubleToIntFunction) {
        if (doubleToIntFunction == null) {
            return null;
        }
        return doubleToIntFunction instanceof u ? ((u) doubleToIntFunction).a : new t(doubleToIntFunction);
    }

    public final /* synthetic */ int a(double d) {
        return this.a.applyAsInt(d);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof t) {
            obj = ((t) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
