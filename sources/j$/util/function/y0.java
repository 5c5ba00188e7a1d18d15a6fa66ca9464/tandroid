package j$.util.function;

/* loaded from: classes2.dex */
public final /* synthetic */ class y0 implements java.util.function.ToIntFunction {
    public final /* synthetic */ ToIntFunction a;

    private /* synthetic */ y0(ToIntFunction toIntFunction) {
        this.a = toIntFunction;
    }

    public static /* synthetic */ java.util.function.ToIntFunction a(ToIntFunction toIntFunction) {
        if (toIntFunction == null) {
            return null;
        }
        return toIntFunction instanceof x0 ? ((x0) toIntFunction).a : new y0(toIntFunction);
    }

    @Override // java.util.function.ToIntFunction
    public final /* synthetic */ int applyAsInt(Object obj) {
        return this.a.applyAsInt(obj);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        ToIntFunction toIntFunction = this.a;
        if (obj instanceof y0) {
            obj = ((y0) obj).a;
        }
        return toIntFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
