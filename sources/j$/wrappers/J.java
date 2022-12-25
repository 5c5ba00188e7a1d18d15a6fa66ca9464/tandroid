package j$.wrappers;

import java.util.function.DoubleUnaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class J {
    final /* synthetic */ DoubleUnaryOperator a;

    private /* synthetic */ J(DoubleUnaryOperator doubleUnaryOperator) {
        this.a = doubleUnaryOperator;
    }

    public static /* synthetic */ J b(DoubleUnaryOperator doubleUnaryOperator) {
        if (doubleUnaryOperator == null) {
            return null;
        }
        return doubleUnaryOperator instanceof K ? ((K) doubleUnaryOperator).a : new J(doubleUnaryOperator);
    }

    public double a(double d) {
        return this.a.applyAsDouble(d);
    }
}
