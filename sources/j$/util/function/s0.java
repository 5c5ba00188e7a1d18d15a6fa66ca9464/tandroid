package j$.util.function;
/* loaded from: classes2.dex */
public final /* synthetic */ class s0 implements java.util.function.Predicate {
    public final /* synthetic */ Predicate a;

    private /* synthetic */ s0(Predicate predicate) {
        this.a = predicate;
    }

    public static /* synthetic */ java.util.function.Predicate a(Predicate predicate) {
        if (predicate == null) {
            return null;
        }
        return predicate instanceof r0 ? ((r0) predicate).a : new s0(predicate);
    }

    @Override // java.util.function.Predicate
    public final /* synthetic */ java.util.function.Predicate and(java.util.function.Predicate predicate) {
        return a(this.a.and(r0.a(predicate)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        Predicate predicate = this.a;
        if (obj instanceof s0) {
            obj = ((s0) obj).a;
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
        return a(this.a.or(r0.a(predicate)));
    }

    @Override // java.util.function.Predicate
    public final /* synthetic */ boolean test(Object obj) {
        return this.a.test(obj);
    }
}
