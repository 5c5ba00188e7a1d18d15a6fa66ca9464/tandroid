package j$.util.function;

/* loaded from: classes2.dex */
public final /* synthetic */ class v0 implements ToDoubleFunction {
    public final /* synthetic */ java.util.function.ToDoubleFunction a;

    private /* synthetic */ v0(java.util.function.ToDoubleFunction toDoubleFunction) {
        this.a = toDoubleFunction;
    }

    public static /* synthetic */ ToDoubleFunction a(java.util.function.ToDoubleFunction toDoubleFunction) {
        if (toDoubleFunction == null) {
            return null;
        }
        return toDoubleFunction instanceof w0 ? ((w0) toDoubleFunction).a : new v0(toDoubleFunction);
    }

    @Override // j$.util.function.ToDoubleFunction
    public final /* synthetic */ double applyAsDouble(Object obj) {
        return this.a.applyAsDouble(obj);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        java.util.function.ToDoubleFunction toDoubleFunction = this.a;
        if (obj instanceof v0) {
            obj = ((v0) obj).a;
        }
        return toDoubleFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
