package j$.util.function;

import java.util.function.LongPredicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class l0 implements LongPredicate {
    public final /* synthetic */ m0 a;

    private /* synthetic */ l0(m0 m0Var) {
        this.a = m0Var;
    }

    public static /* synthetic */ LongPredicate a(m0 m0Var) {
        if (m0Var == null) {
            return null;
        }
        return m0Var instanceof k0 ? ((k0) m0Var).a : new l0(m0Var);
    }

    @Override // java.util.function.LongPredicate
    public final /* synthetic */ LongPredicate and(LongPredicate longPredicate) {
        return a(((k0) this.a).a(k0.b(longPredicate)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        m0 m0Var = this.a;
        if (obj instanceof l0) {
            obj = ((l0) obj).a;
        }
        return m0Var.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // java.util.function.LongPredicate
    public final /* synthetic */ LongPredicate negate() {
        return a(((k0) this.a).c());
    }

    @Override // java.util.function.LongPredicate
    public final /* synthetic */ LongPredicate or(LongPredicate longPredicate) {
        return a(((k0) this.a).d(k0.b(longPredicate)));
    }

    @Override // java.util.function.LongPredicate
    public final /* synthetic */ boolean test(long j) {
        return ((k0) this.a).e(j);
    }
}
