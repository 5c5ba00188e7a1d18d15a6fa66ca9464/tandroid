package j$.util.function;

import java.util.function.IntUnaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class Z implements IntUnaryOperator {
    public final /* synthetic */ a0 a;

    private /* synthetic */ Z(a0 a0Var) {
        this.a = a0Var;
    }

    public static /* synthetic */ IntUnaryOperator a(a0 a0Var) {
        if (a0Var == null) {
            return null;
        }
        return a0Var instanceof Y ? ((Y) a0Var).a : new Z(a0Var);
    }

    @Override // java.util.function.IntUnaryOperator
    public final /* synthetic */ IntUnaryOperator andThen(IntUnaryOperator intUnaryOperator) {
        return a(((Y) this.a).a(Y.d(intUnaryOperator)));
    }

    @Override // java.util.function.IntUnaryOperator
    public final /* synthetic */ int applyAsInt(int i) {
        return ((Y) this.a).b(i);
    }

    @Override // java.util.function.IntUnaryOperator
    public final /* synthetic */ IntUnaryOperator compose(IntUnaryOperator intUnaryOperator) {
        return a(((Y) this.a).c(Y.d(intUnaryOperator)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        a0 a0Var = this.a;
        if (obj instanceof Z) {
            obj = ((Z) obj).a;
        }
        return a0Var.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
