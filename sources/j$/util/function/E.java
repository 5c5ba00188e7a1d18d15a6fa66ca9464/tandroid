package j$.util.function;

import java.util.function.IntBinaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class E implements G {
    public final /* synthetic */ IntBinaryOperator a;

    private /* synthetic */ E(IntBinaryOperator intBinaryOperator) {
        this.a = intBinaryOperator;
    }

    public static /* synthetic */ G a(IntBinaryOperator intBinaryOperator) {
        if (intBinaryOperator == null) {
            return null;
        }
        return intBinaryOperator instanceof F ? ((F) intBinaryOperator).a : new E(intBinaryOperator);
    }

    @Override // j$.util.function.G
    public final /* synthetic */ int applyAsInt(int i, int i2) {
        return this.a.applyAsInt(i, i2);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof E) {
            obj = ((E) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
