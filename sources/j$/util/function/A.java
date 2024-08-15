package j$.util.function;

import java.util.function.DoubleUnaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class A implements DoubleUnaryOperator {
    public final /* synthetic */ B a;

    private /* synthetic */ A(B b) {
        this.a = b;
    }

    public static /* synthetic */ DoubleUnaryOperator a(B b) {
        if (b == null) {
            return null;
        }
        return b instanceof z ? ((z) b).a : new A(b);
    }

    @Override // java.util.function.DoubleUnaryOperator
    public final /* synthetic */ DoubleUnaryOperator andThen(DoubleUnaryOperator doubleUnaryOperator) {
        return a(((z) this.a).a(z.d(doubleUnaryOperator)));
    }

    @Override // java.util.function.DoubleUnaryOperator
    public final /* synthetic */ double applyAsDouble(double d) {
        return ((z) this.a).b(d);
    }

    @Override // java.util.function.DoubleUnaryOperator
    public final /* synthetic */ DoubleUnaryOperator compose(DoubleUnaryOperator doubleUnaryOperator) {
        return a(((z) this.a).c(z.d(doubleUnaryOperator)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        B b = this.a;
        if (obj instanceof A) {
            obj = ((A) obj).a;
        }
        return b.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
