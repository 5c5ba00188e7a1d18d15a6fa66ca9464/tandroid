package j$.util.function;

import j$.util.function.Function;
import java.util.function.UnaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class S0 implements U0 {
    public final /* synthetic */ UnaryOperator a;

    private /* synthetic */ S0(UnaryOperator unaryOperator) {
        this.a = unaryOperator;
    }

    public static /* synthetic */ U0 a(UnaryOperator unaryOperator) {
        if (unaryOperator == null) {
            return null;
        }
        return unaryOperator instanceof T0 ? ((T0) unaryOperator).a : new S0(unaryOperator);
    }

    @Override // j$.util.function.Function
    public final /* synthetic */ Function andThen(Function function) {
        return Function.VivifiedWrapper.convert(this.a.andThen(D.a(function)));
    }

    @Override // j$.util.function.Function
    public final /* synthetic */ Object apply(Object obj) {
        return this.a.apply(obj);
    }

    @Override // j$.util.function.Function
    public final /* synthetic */ Function compose(Function function) {
        return Function.VivifiedWrapper.convert(this.a.compose(D.a(function)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof S0) {
            obj = ((S0) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
