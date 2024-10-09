package j$.util.function;

/* loaded from: classes2.dex */
public final /* synthetic */ class w0 implements java.util.function.ToDoubleFunction {
    public final /* synthetic */ ToDoubleFunction a;

    private /* synthetic */ w0(ToDoubleFunction toDoubleFunction) {
        this.a = toDoubleFunction;
    }

    public static /* synthetic */ java.util.function.ToDoubleFunction a(ToDoubleFunction toDoubleFunction) {
        if (toDoubleFunction == null) {
            return null;
        }
        return toDoubleFunction instanceof v0 ? ((v0) toDoubleFunction).a : new w0(toDoubleFunction);
    }

    @Override // java.util.function.ToDoubleFunction
    public final /* synthetic */ double applyAsDouble(Object obj) {
        return this.a.applyAsDouble(obj);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        ToDoubleFunction toDoubleFunction = this.a;
        if (obj instanceof w0) {
            obj = ((w0) obj).a;
        }
        return toDoubleFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
