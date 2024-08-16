package j$.util.function;

import java.util.function.DoubleUnaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class w {
    public final /* synthetic */ DoubleUnaryOperator a;

    private /* synthetic */ w(DoubleUnaryOperator doubleUnaryOperator) {
        this.a = doubleUnaryOperator;
    }

    public static /* synthetic */ w a(DoubleUnaryOperator doubleUnaryOperator) {
        if (doubleUnaryOperator == null) {
            return null;
        }
        return new w(doubleUnaryOperator);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        DoubleUnaryOperator doubleUnaryOperator = this.a;
        if (obj instanceof w) {
            obj = ((w) obj).a;
        }
        return doubleUnaryOperator.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
