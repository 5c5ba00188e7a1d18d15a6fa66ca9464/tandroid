package j$.util.function;

import java.util.function.DoubleBinaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class g implements i {
    public final /* synthetic */ DoubleBinaryOperator a;

    private /* synthetic */ g(DoubleBinaryOperator doubleBinaryOperator) {
        this.a = doubleBinaryOperator;
    }

    public static /* synthetic */ i a(DoubleBinaryOperator doubleBinaryOperator) {
        if (doubleBinaryOperator == null) {
            return null;
        }
        return doubleBinaryOperator instanceof h ? ((h) doubleBinaryOperator).a : new g(doubleBinaryOperator);
    }

    @Override // j$.util.function.i
    public final /* synthetic */ double applyAsDouble(double d, double d2) {
        return this.a.applyAsDouble(d, d2);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof g) {
            obj = ((g) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
