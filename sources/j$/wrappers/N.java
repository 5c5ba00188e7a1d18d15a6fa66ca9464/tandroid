package j$.wrappers;

import java.util.function.IntBinaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class N implements j$.util.function.j {
    final /* synthetic */ IntBinaryOperator a;

    private /* synthetic */ N(IntBinaryOperator intBinaryOperator) {
        this.a = intBinaryOperator;
    }

    public static /* synthetic */ j$.util.function.j a(IntBinaryOperator intBinaryOperator) {
        if (intBinaryOperator == null) {
            return null;
        }
        return intBinaryOperator instanceof O ? ((O) intBinaryOperator).a : new N(intBinaryOperator);
    }

    @Override // j$.util.function.j
    public /* synthetic */ int applyAsInt(int i, int i2) {
        return this.a.applyAsInt(i, i2);
    }
}
