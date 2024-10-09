package j$.util.function;

import java.util.function.DoubleToLongFunction;

/* loaded from: classes2.dex */
public final /* synthetic */ class u implements DoubleToLongFunction {
    public final /* synthetic */ v a;

    private /* synthetic */ u(v vVar) {
        this.a = vVar;
    }

    public static /* synthetic */ DoubleToLongFunction a(v vVar) {
        if (vVar == null) {
            return null;
        }
        return vVar instanceof t ? ((t) vVar).a : new u(vVar);
    }

    @Override // java.util.function.DoubleToLongFunction
    public final /* synthetic */ long applyAsLong(double d) {
        return this.a.applyAsLong(d);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        v vVar = this.a;
        if (obj instanceof u) {
            obj = ((u) obj).a;
        }
        return vVar.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
