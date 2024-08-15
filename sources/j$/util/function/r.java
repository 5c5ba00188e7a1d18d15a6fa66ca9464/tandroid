package j$.util.function;

import java.util.function.DoublePredicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class r implements DoublePredicate {
    public final /* synthetic */ s a;

    private /* synthetic */ r(s sVar) {
        this.a = sVar;
    }

    public static /* synthetic */ DoublePredicate a(s sVar) {
        if (sVar == null) {
            return null;
        }
        return sVar instanceof q ? ((q) sVar).a : new r(sVar);
    }

    @Override // java.util.function.DoublePredicate
    public final /* synthetic */ DoublePredicate and(DoublePredicate doublePredicate) {
        return a(((q) this.a).a(q.b(doublePredicate)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        s sVar = this.a;
        if (obj instanceof r) {
            obj = ((r) obj).a;
        }
        return sVar.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // java.util.function.DoublePredicate
    public final /* synthetic */ DoublePredicate negate() {
        return a(((q) this.a).c());
    }

    @Override // java.util.function.DoublePredicate
    public final /* synthetic */ DoublePredicate or(DoublePredicate doublePredicate) {
        return a(((q) this.a).d(q.b(doublePredicate)));
    }

    @Override // java.util.function.DoublePredicate
    public final /* synthetic */ boolean test(double d) {
        return ((q) this.a).e(d);
    }
}
