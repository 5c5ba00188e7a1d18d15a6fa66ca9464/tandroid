package j$.util.function;

import java.util.function.DoublePredicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class q implements s {
    public final /* synthetic */ DoublePredicate a;

    private /* synthetic */ q(DoublePredicate doublePredicate) {
        this.a = doublePredicate;
    }

    public static /* synthetic */ s b(DoublePredicate doublePredicate) {
        if (doublePredicate == null) {
            return null;
        }
        return doublePredicate instanceof r ? ((r) doublePredicate).a : new q(doublePredicate);
    }

    public final /* synthetic */ s a(s sVar) {
        return b(this.a.and(r.a(sVar)));
    }

    public final /* synthetic */ s c() {
        return b(this.a.negate());
    }

    public final /* synthetic */ s d(s sVar) {
        return b(this.a.or(r.a(sVar)));
    }

    public final /* synthetic */ boolean e(double d) {
        return this.a.test(d);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof q) {
            obj = ((q) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
