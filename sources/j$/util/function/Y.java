package j$.util.function;

import java.util.function.IntUnaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class Y implements a0 {
    public final /* synthetic */ IntUnaryOperator a;

    private /* synthetic */ Y(IntUnaryOperator intUnaryOperator) {
        this.a = intUnaryOperator;
    }

    public static /* synthetic */ a0 d(IntUnaryOperator intUnaryOperator) {
        if (intUnaryOperator == null) {
            return null;
        }
        return intUnaryOperator instanceof Z ? ((Z) intUnaryOperator).a : new Y(intUnaryOperator);
    }

    public final /* synthetic */ a0 a(a0 a0Var) {
        return d(this.a.andThen(Z.a(a0Var)));
    }

    public final /* synthetic */ int b(int i) {
        return this.a.applyAsInt(i);
    }

    public final /* synthetic */ a0 c(a0 a0Var) {
        return d(this.a.compose(Z.a(a0Var)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof Y) {
            obj = ((Y) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
