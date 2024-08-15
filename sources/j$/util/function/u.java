package j$.util.function;

import java.util.function.DoubleToIntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class u implements DoubleToIntFunction {
    public final /* synthetic */ v a;

    private /* synthetic */ u(v vVar) {
        this.a = vVar;
    }

    public static /* synthetic */ DoubleToIntFunction a(v vVar) {
        if (vVar == null) {
            return null;
        }
        return vVar instanceof t ? ((t) vVar).a : new u(vVar);
    }

    @Override // java.util.function.DoubleToIntFunction
    public final /* synthetic */ int applyAsInt(double d) {
        return ((t) this.a).a(d);
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
