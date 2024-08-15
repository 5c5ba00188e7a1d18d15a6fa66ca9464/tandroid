package j$.util.function;

import java.util.function.IntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class L implements N {
    public final /* synthetic */ IntFunction a;

    private /* synthetic */ L(IntFunction intFunction) {
        this.a = intFunction;
    }

    public static /* synthetic */ N a(IntFunction intFunction) {
        if (intFunction == null) {
            return null;
        }
        return intFunction instanceof M ? ((M) intFunction).a : new L(intFunction);
    }

    @Override // j$.util.function.N
    public final /* synthetic */ Object apply(int i) {
        return this.a.apply(i);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof L) {
            obj = ((L) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
