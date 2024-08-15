package j$.util.function;
/* loaded from: classes2.dex */
public final /* synthetic */ class M0 implements ToDoubleFunction {
    public final /* synthetic */ java.util.function.ToDoubleFunction a;

    private /* synthetic */ M0(java.util.function.ToDoubleFunction toDoubleFunction) {
        this.a = toDoubleFunction;
    }

    public static /* synthetic */ ToDoubleFunction a(java.util.function.ToDoubleFunction toDoubleFunction) {
        if (toDoubleFunction == null) {
            return null;
        }
        return toDoubleFunction instanceof N0 ? ((N0) toDoubleFunction).a : new M0(toDoubleFunction);
    }

    @Override // j$.util.function.ToDoubleFunction
    public final /* synthetic */ double applyAsDouble(Object obj) {
        return this.a.applyAsDouble(obj);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof M0) {
            obj = ((M0) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
