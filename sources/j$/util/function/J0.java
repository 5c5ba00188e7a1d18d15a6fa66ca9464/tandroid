package j$.util.function;
/* loaded from: classes2.dex */
public final /* synthetic */ class J0 implements java.util.function.Predicate {
    public final /* synthetic */ Predicate a;

    private /* synthetic */ J0(Predicate predicate) {
        this.a = predicate;
    }

    public static /* synthetic */ java.util.function.Predicate a(Predicate predicate) {
        if (predicate == null) {
            return null;
        }
        return predicate instanceof I0 ? ((I0) predicate).a : new J0(predicate);
    }

    @Override // java.util.function.Predicate
    public final /* synthetic */ java.util.function.Predicate and(java.util.function.Predicate predicate) {
        return a(this.a.and(I0.a(predicate)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        Predicate predicate = this.a;
        if (obj instanceof J0) {
            obj = ((J0) obj).a;
        }
        return predicate.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // java.util.function.Predicate
    public final /* synthetic */ java.util.function.Predicate negate() {
        return a(this.a.negate());
    }

    @Override // java.util.function.Predicate
    public final /* synthetic */ java.util.function.Predicate or(java.util.function.Predicate predicate) {
        return a(this.a.or(I0.a(predicate)));
    }

    @Override // java.util.function.Predicate
    public final /* synthetic */ boolean test(Object obj) {
        return this.a.test(obj);
    }
}
