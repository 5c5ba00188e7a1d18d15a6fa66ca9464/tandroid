package j$.util.function;

import java.util.function.DoubleBinaryOperator;

/* loaded from: classes2.dex */
public final /* synthetic */ class h implements j {
    public final /* synthetic */ DoubleBinaryOperator a;

    private /* synthetic */ h(DoubleBinaryOperator doubleBinaryOperator) {
        this.a = doubleBinaryOperator;
    }

    public static /* synthetic */ j a(DoubleBinaryOperator doubleBinaryOperator) {
        if (doubleBinaryOperator == null) {
            return null;
        }
        return doubleBinaryOperator instanceof i ? ((i) doubleBinaryOperator).a : new h(doubleBinaryOperator);
    }

    @Override // j$.util.function.j
    public final /* synthetic */ double applyAsDouble(double d, double d2) {
        return this.a.applyAsDouble(d, d2);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        DoubleBinaryOperator doubleBinaryOperator = this.a;
        if (obj instanceof h) {
            obj = ((h) obj).a;
        }
        return doubleBinaryOperator.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
