package j$.util.function;

import java.util.function.DoubleUnaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class z implements B {
    public final /* synthetic */ DoubleUnaryOperator a;

    private /* synthetic */ z(DoubleUnaryOperator doubleUnaryOperator) {
        this.a = doubleUnaryOperator;
    }

    public static /* synthetic */ B d(DoubleUnaryOperator doubleUnaryOperator) {
        if (doubleUnaryOperator == null) {
            return null;
        }
        return doubleUnaryOperator instanceof A ? ((A) doubleUnaryOperator).a : new z(doubleUnaryOperator);
    }

    public final /* synthetic */ B a(B b) {
        return d(this.a.andThen(A.a(b)));
    }

    public final /* synthetic */ double b(double d) {
        return this.a.applyAsDouble(d);
    }

    public final /* synthetic */ B c(B b) {
        return d(this.a.compose(A.a(b)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof z) {
            obj = ((z) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
