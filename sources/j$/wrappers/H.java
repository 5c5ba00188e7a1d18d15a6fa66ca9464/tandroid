package j$.wrappers;

import java.util.function.DoubleToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class H implements j$.util.function.h {
    final /* synthetic */ DoubleToLongFunction a;

    private /* synthetic */ H(DoubleToLongFunction doubleToLongFunction) {
        this.a = doubleToLongFunction;
    }

    public static /* synthetic */ j$.util.function.h a(DoubleToLongFunction doubleToLongFunction) {
        if (doubleToLongFunction == null) {
            return null;
        }
        return doubleToLongFunction instanceof I ? ((I) doubleToLongFunction).a : new H(doubleToLongFunction);
    }

    @Override // j$.util.function.h
    public /* synthetic */ long applyAsLong(double d) {
        return this.a.applyAsLong(d);
    }
}
