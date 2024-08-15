package j$.util.function;
/* loaded from: classes2.dex */
public final /* synthetic */ class O0 implements ToIntFunction {
    public final /* synthetic */ java.util.function.ToIntFunction a;

    private /* synthetic */ O0(java.util.function.ToIntFunction toIntFunction) {
        this.a = toIntFunction;
    }

    public static /* synthetic */ ToIntFunction a(java.util.function.ToIntFunction toIntFunction) {
        if (toIntFunction == null) {
            return null;
        }
        return toIntFunction instanceof P0 ? ((P0) toIntFunction).a : new O0(toIntFunction);
    }

    @Override // j$.util.function.ToIntFunction
    public final /* synthetic */ int applyAsInt(Object obj) {
        return this.a.applyAsInt(obj);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof O0) {
            obj = ((O0) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
