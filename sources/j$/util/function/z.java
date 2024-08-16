package j$.util.function;

import java.util.function.IntBinaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class z implements B {
    public final /* synthetic */ IntBinaryOperator a;

    private /* synthetic */ z(IntBinaryOperator intBinaryOperator) {
        this.a = intBinaryOperator;
    }

    public static /* synthetic */ B a(IntBinaryOperator intBinaryOperator) {
        if (intBinaryOperator == null) {
            return null;
        }
        return intBinaryOperator instanceof A ? ((A) intBinaryOperator).a : new z(intBinaryOperator);
    }

    @Override // j$.util.function.B
    public final /* synthetic */ int applyAsInt(int i, int i2) {
        return this.a.applyAsInt(i, i2);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        IntBinaryOperator intBinaryOperator = this.a;
        if (obj instanceof z) {
            obj = ((z) obj).a;
        }
        return intBinaryOperator.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
