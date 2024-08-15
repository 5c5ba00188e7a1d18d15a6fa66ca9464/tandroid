package j$.util.function;

import j$.util.function.Function;
import java.util.function.BinaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class e implements BinaryOperator {
    public final /* synthetic */ f a;

    private /* synthetic */ e(f fVar) {
        this.a = fVar;
    }

    public static /* synthetic */ BinaryOperator a(f fVar) {
        if (fVar == null) {
            return null;
        }
        return fVar instanceof d ? ((d) fVar).a : new e(fVar);
    }

    @Override // java.util.function.BiFunction
    public final /* synthetic */ java.util.function.BiFunction andThen(java.util.function.Function function) {
        return b.a(this.a.andThen(Function.VivifiedWrapper.convert(function)));
    }

    @Override // java.util.function.BiFunction
    public final /* synthetic */ Object apply(Object obj, Object obj2) {
        return this.a.apply(obj, obj2);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        f fVar = this.a;
        if (obj instanceof e) {
            obj = ((e) obj).a;
        }
        return fVar.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
