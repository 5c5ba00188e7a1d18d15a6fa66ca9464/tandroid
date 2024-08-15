package j$.util.function;
/* loaded from: classes2.dex */
public final /* synthetic */ class I0 implements Predicate {
    public final /* synthetic */ java.util.function.Predicate a;

    private /* synthetic */ I0(java.util.function.Predicate predicate) {
        this.a = predicate;
    }

    public static /* synthetic */ Predicate a(java.util.function.Predicate predicate) {
        if (predicate == null) {
            return null;
        }
        return predicate instanceof J0 ? ((J0) predicate).a : new I0(predicate);
    }

    @Override // j$.util.function.Predicate
    public final /* synthetic */ Predicate and(Predicate predicate) {
        return a(this.a.and(J0.a(predicate)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof I0) {
            obj = ((I0) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.function.Predicate
    public final /* synthetic */ Predicate negate() {
        return a(this.a.negate());
    }

    @Override // j$.util.function.Predicate
    public final /* synthetic */ Predicate or(Predicate predicate) {
        return a(this.a.or(J0.a(predicate)));
    }

    @Override // j$.util.function.Predicate
    public final /* synthetic */ boolean test(Object obj) {
        return this.a.test(obj);
    }
}
