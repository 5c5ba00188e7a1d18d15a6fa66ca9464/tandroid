package j$.util.function;

/* loaded from: classes2.dex */
public final /* synthetic */ class X implements LongFunction {
    public final /* synthetic */ java.util.function.LongFunction a;

    private /* synthetic */ X(java.util.function.LongFunction longFunction) {
        this.a = longFunction;
    }

    public static /* synthetic */ LongFunction a(java.util.function.LongFunction longFunction) {
        if (longFunction == null) {
            return null;
        }
        return longFunction instanceof Y ? ((Y) longFunction).a : new X(longFunction);
    }

    @Override // j$.util.function.LongFunction
    public final /* synthetic */ Object apply(long j) {
        return this.a.apply(j);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        java.util.function.LongFunction longFunction = this.a;
        if (obj instanceof X) {
            obj = ((X) obj).a;
        }
        return longFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
