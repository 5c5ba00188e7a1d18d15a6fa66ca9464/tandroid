package j$.util.function;
/* loaded from: classes2.dex */
public final /* synthetic */ class A0 implements java.util.function.ToLongFunction {
    public final /* synthetic */ ToLongFunction a;

    private /* synthetic */ A0(ToLongFunction toLongFunction) {
        this.a = toLongFunction;
    }

    public static /* synthetic */ java.util.function.ToLongFunction a(ToLongFunction toLongFunction) {
        if (toLongFunction == null) {
            return null;
        }
        return toLongFunction instanceof z0 ? ((z0) toLongFunction).a : new A0(toLongFunction);
    }

    @Override // java.util.function.ToLongFunction
    public final /* synthetic */ long applyAsLong(Object obj) {
        return this.a.applyAsLong(obj);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        ToLongFunction toLongFunction = this.a;
        if (obj instanceof A0) {
            obj = ((A0) obj).a;
        }
        return toLongFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
