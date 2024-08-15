package j$.util.function;

import j$.util.function.Function;
import java.util.function.UnaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class T0 implements UnaryOperator {
    public final /* synthetic */ U0 a;

    private /* synthetic */ T0(U0 u0) {
        this.a = u0;
    }

    public static /* synthetic */ UnaryOperator a(U0 u0) {
        if (u0 == null) {
            return null;
        }
        return u0 instanceof S0 ? ((S0) u0).a : new T0(u0);
    }

    @Override // java.util.function.Function
    public final /* synthetic */ java.util.function.Function andThen(java.util.function.Function function) {
        return D.a(((S0) this.a).andThen(Function.VivifiedWrapper.convert(function)));
    }

    @Override // java.util.function.Function
    public final /* synthetic */ Object apply(Object obj) {
        return ((S0) this.a).apply(obj);
    }

    @Override // java.util.function.Function
    public final /* synthetic */ java.util.function.Function compose(java.util.function.Function function) {
        return D.a(((S0) this.a).compose(Function.VivifiedWrapper.convert(function)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        U0 u0 = this.a;
        if (obj instanceof T0) {
            obj = ((T0) obj).a;
        }
        return u0.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
