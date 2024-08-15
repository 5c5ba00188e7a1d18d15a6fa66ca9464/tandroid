package j$.util.function;

import java.util.function.DoubleToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class x implements DoubleToLongFunction {
    public final /* synthetic */ y a;

    private /* synthetic */ x(y yVar) {
        this.a = yVar;
    }

    public static /* synthetic */ DoubleToLongFunction a(y yVar) {
        if (yVar == null) {
            return null;
        }
        return yVar instanceof w ? ((w) yVar).a : new x(yVar);
    }

    @Override // java.util.function.DoubleToLongFunction
    public final /* synthetic */ long applyAsLong(double d) {
        return this.a.applyAsLong(d);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        y yVar = this.a;
        if (obj instanceof x) {
            obj = ((x) obj).a;
        }
        return yVar.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
