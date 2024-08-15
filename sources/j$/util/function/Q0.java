package j$.util.function;
/* loaded from: classes2.dex */
public final /* synthetic */ class Q0 implements ToLongFunction {
    public final /* synthetic */ java.util.function.ToLongFunction a;

    private /* synthetic */ Q0(java.util.function.ToLongFunction toLongFunction) {
        this.a = toLongFunction;
    }

    public static /* synthetic */ ToLongFunction a(java.util.function.ToLongFunction toLongFunction) {
        if (toLongFunction == null) {
            return null;
        }
        return toLongFunction instanceof R0 ? ((R0) toLongFunction).a : new Q0(toLongFunction);
    }

    @Override // j$.util.function.ToLongFunction
    public final /* synthetic */ long applyAsLong(Object obj) {
        return this.a.applyAsLong(obj);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof Q0) {
            obj = ((Q0) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
