package j$.util.function;
/* loaded from: classes2.dex */
public final /* synthetic */ class j0 implements java.util.function.LongFunction {
    public final /* synthetic */ LongFunction a;

    private /* synthetic */ j0(LongFunction longFunction) {
        this.a = longFunction;
    }

    public static /* synthetic */ java.util.function.LongFunction a(LongFunction longFunction) {
        if (longFunction == null) {
            return null;
        }
        return longFunction instanceof i0 ? ((i0) longFunction).a : new j0(longFunction);
    }

    @Override // java.util.function.LongFunction
    public final /* synthetic */ Object apply(long j) {
        return this.a.apply(j);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        LongFunction longFunction = this.a;
        if (obj instanceof j0) {
            obj = ((j0) obj).a;
        }
        return longFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
