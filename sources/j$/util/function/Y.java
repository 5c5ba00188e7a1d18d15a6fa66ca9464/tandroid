package j$.util.function;
/* loaded from: classes2.dex */
public final /* synthetic */ class Y implements java.util.function.LongFunction {
    public final /* synthetic */ LongFunction a;

    private /* synthetic */ Y(LongFunction longFunction) {
        this.a = longFunction;
    }

    public static /* synthetic */ java.util.function.LongFunction a(LongFunction longFunction) {
        if (longFunction == null) {
            return null;
        }
        return longFunction instanceof X ? ((X) longFunction).a : new Y(longFunction);
    }

    @Override // java.util.function.LongFunction
    public final /* synthetic */ Object apply(long j) {
        return this.a.apply(j);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        LongFunction longFunction = this.a;
        if (obj instanceof Y) {
            obj = ((Y) obj).a;
        }
        return longFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
