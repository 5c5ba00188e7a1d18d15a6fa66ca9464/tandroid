package j$.util.function;
/* loaded from: classes2.dex */
public final /* synthetic */ class z0 implements ToLongFunction {
    public final /* synthetic */ java.util.function.ToLongFunction a;

    private /* synthetic */ z0(java.util.function.ToLongFunction toLongFunction) {
        this.a = toLongFunction;
    }

    public static /* synthetic */ ToLongFunction a(java.util.function.ToLongFunction toLongFunction) {
        if (toLongFunction == null) {
            return null;
        }
        return toLongFunction instanceof A0 ? ((A0) toLongFunction).a : new z0(toLongFunction);
    }

    @Override // j$.util.function.ToLongFunction
    public final /* synthetic */ long applyAsLong(Object obj) {
        return this.a.applyAsLong(obj);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        java.util.function.ToLongFunction toLongFunction = this.a;
        if (obj instanceof z0) {
            obj = ((z0) obj).a;
        }
        return toLongFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
