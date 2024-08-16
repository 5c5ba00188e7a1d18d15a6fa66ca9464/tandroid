package j$.util.function;

import java.util.function.IntUnaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class O {
    public final /* synthetic */ IntUnaryOperator a;

    private /* synthetic */ O(IntUnaryOperator intUnaryOperator) {
        this.a = intUnaryOperator;
    }

    public static /* synthetic */ O a(IntUnaryOperator intUnaryOperator) {
        if (intUnaryOperator == null) {
            return null;
        }
        return new O(intUnaryOperator);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        IntUnaryOperator intUnaryOperator = this.a;
        if (obj instanceof O) {
            obj = ((O) obj).a;
        }
        return intUnaryOperator.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
