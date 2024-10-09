package j$.util.function;

/* loaded from: classes2.dex */
public final /* synthetic */ class x0 implements ToIntFunction {
    public final /* synthetic */ java.util.function.ToIntFunction a;

    private /* synthetic */ x0(java.util.function.ToIntFunction toIntFunction) {
        this.a = toIntFunction;
    }

    public static /* synthetic */ ToIntFunction a(java.util.function.ToIntFunction toIntFunction) {
        if (toIntFunction == null) {
            return null;
        }
        return toIntFunction instanceof y0 ? ((y0) toIntFunction).a : new x0(toIntFunction);
    }

    @Override // j$.util.function.ToIntFunction
    public final /* synthetic */ int applyAsInt(Object obj) {
        return this.a.applyAsInt(obj);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        java.util.function.ToIntFunction toIntFunction = this.a;
        if (obj instanceof x0) {
            obj = ((x0) obj).a;
        }
        return toIntFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
