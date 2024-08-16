package j$.util.function;

import j$.util.function.Function;
import java.util.function.UnaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class B0 implements Function {
    public final /* synthetic */ UnaryOperator a;

    private /* synthetic */ B0(UnaryOperator unaryOperator) {
        this.a = unaryOperator;
    }

    public static /* synthetic */ B0 a(UnaryOperator unaryOperator) {
        if (unaryOperator == null) {
            return null;
        }
        return new B0(unaryOperator);
    }

    @Override // j$.util.function.Function
    public final /* synthetic */ Function andThen(Function function) {
        return Function.VivifiedWrapper.convert(this.a.andThen(y.a(function)));
    }

    @Override // j$.util.function.Function
    public final /* synthetic */ Object apply(Object obj) {
        return this.a.apply(obj);
    }

    @Override // j$.util.function.Function
    public final /* synthetic */ Function compose(Function function) {
        return Function.VivifiedWrapper.convert(this.a.compose(y.a(function)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        UnaryOperator unaryOperator = this.a;
        if (obj instanceof B0) {
            obj = ((B0) obj).a;
        }
        return unaryOperator.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
