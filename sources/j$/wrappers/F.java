package j$.wrappers;

import java.util.function.DoubleToIntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class F {
    final /* synthetic */ DoubleToIntFunction a;

    private /* synthetic */ F(DoubleToIntFunction doubleToIntFunction) {
        this.a = doubleToIntFunction;
    }

    public static /* synthetic */ F b(DoubleToIntFunction doubleToIntFunction) {
        if (doubleToIntFunction == null) {
            return null;
        }
        return doubleToIntFunction instanceof G ? ((G) doubleToIntFunction).a : new F(doubleToIntFunction);
    }

    public int a(double d) {
        return this.a.applyAsInt(d);
    }
}
