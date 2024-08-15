package j$.util.function;

import java.util.function.IntPredicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class P implements IntPredicate {
    public final /* synthetic */ Q a;

    private /* synthetic */ P(Q q) {
        this.a = q;
    }

    public static /* synthetic */ IntPredicate a(Q q) {
        if (q == null) {
            return null;
        }
        return q instanceof O ? ((O) q).a : new P(q);
    }

    @Override // java.util.function.IntPredicate
    public final /* synthetic */ IntPredicate and(IntPredicate intPredicate) {
        return a(((O) this.a).a(O.b(intPredicate)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        Q q = this.a;
        if (obj instanceof P) {
            obj = ((P) obj).a;
        }
        return q.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // java.util.function.IntPredicate
    public final /* synthetic */ IntPredicate negate() {
        return a(((O) this.a).c());
    }

    @Override // java.util.function.IntPredicate
    public final /* synthetic */ IntPredicate or(IntPredicate intPredicate) {
        return a(((O) this.a).d(O.b(intPredicate)));
    }

    @Override // java.util.function.IntPredicate
    public final /* synthetic */ boolean test(int i) {
        return ((O) this.a).e(i);
    }
}
