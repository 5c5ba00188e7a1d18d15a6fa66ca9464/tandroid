package j$.util.function;

/* loaded from: classes2.dex */
public final /* synthetic */ class r0 implements Predicate {
    public final /* synthetic */ java.util.function.Predicate a;

    private /* synthetic */ r0(java.util.function.Predicate predicate) {
        this.a = predicate;
    }

    public static /* synthetic */ Predicate a(java.util.function.Predicate predicate) {
        if (predicate == null) {
            return null;
        }
        return predicate instanceof s0 ? ((s0) predicate).a : new r0(predicate);
    }

    @Override // j$.util.function.Predicate
    public final /* synthetic */ Predicate and(Predicate predicate) {
        return a(this.a.and(s0.a(predicate)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        java.util.function.Predicate predicate = this.a;
        if (obj instanceof r0) {
            obj = ((r0) obj).a;
        }
        return predicate.equals(obj);
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
        return a(this.a.or(s0.a(predicate)));
    }

    @Override // j$.util.function.Predicate
    public final /* synthetic */ boolean test(Object obj) {
        return this.a.test(obj);
    }
}
