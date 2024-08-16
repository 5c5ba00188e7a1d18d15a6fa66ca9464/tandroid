package j$.util.function;

import java.util.function.IntToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class M implements IntToLongFunction {
    public final /* synthetic */ N a;

    private /* synthetic */ M(N n) {
        this.a = n;
    }

    public static /* synthetic */ IntToLongFunction a(N n) {
        if (n == null) {
            return null;
        }
        return n instanceof L ? ((L) n).a : new M(n);
    }

    @Override // java.util.function.IntToLongFunction
    public final /* synthetic */ long applyAsLong(int i) {
        return this.a.applyAsLong(i);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        N n = this.a;
        if (obj instanceof M) {
            obj = ((M) obj).a;
        }
        return n.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
