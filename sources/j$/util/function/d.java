package j$.util.function;

import j$.util.function.BiFunction;
import java.util.function.BinaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class d implements f {
    public final /* synthetic */ BinaryOperator a;

    private /* synthetic */ d(BinaryOperator binaryOperator) {
        this.a = binaryOperator;
    }

    public static /* synthetic */ f a(BinaryOperator binaryOperator) {
        if (binaryOperator == null) {
            return null;
        }
        return binaryOperator instanceof e ? ((e) binaryOperator).a : new d(binaryOperator);
    }

    @Override // j$.util.function.BiFunction
    public final /* synthetic */ BiFunction andThen(Function function) {
        return BiFunction.VivifiedWrapper.convert(this.a.andThen(D.a(function)));
    }

    @Override // j$.util.function.BiFunction
    public final /* synthetic */ Object apply(Object obj, Object obj2) {
        return this.a.apply(obj, obj2);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof d) {
            obj = ((d) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
