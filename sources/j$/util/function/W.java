package j$.util.function;

import java.util.function.IntToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class W implements IntToLongFunction {
    public final /* synthetic */ X a;

    private /* synthetic */ W(X x) {
        this.a = x;
    }

    public static /* synthetic */ IntToLongFunction a(X x) {
        if (x == null) {
            return null;
        }
        return x instanceof V ? ((V) x).a : new W(x);
    }

    @Override // java.util.function.IntToLongFunction
    public final /* synthetic */ long applyAsLong(int i) {
        return this.a.applyAsLong(i);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        X x = this.a;
        if (obj instanceof W) {
            obj = ((W) obj).a;
        }
        return x.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
