package j$.util.function;
/* loaded from: classes2.dex */
public final /* synthetic */ class i0 implements LongFunction {
    public final /* synthetic */ java.util.function.LongFunction a;

    private /* synthetic */ i0(java.util.function.LongFunction longFunction) {
        this.a = longFunction;
    }

    public static /* synthetic */ LongFunction a(java.util.function.LongFunction longFunction) {
        if (longFunction == null) {
            return null;
        }
        return longFunction instanceof j0 ? ((j0) longFunction).a : new i0(longFunction);
    }

    @Override // j$.util.function.LongFunction
    public final /* synthetic */ Object apply(long j) {
        return this.a.apply(j);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof i0) {
            obj = ((i0) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
