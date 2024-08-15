package j$.util.function;

import java.util.function.LongPredicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class k0 implements m0 {
    public final /* synthetic */ LongPredicate a;

    private /* synthetic */ k0(LongPredicate longPredicate) {
        this.a = longPredicate;
    }

    public static /* synthetic */ m0 b(LongPredicate longPredicate) {
        if (longPredicate == null) {
            return null;
        }
        return longPredicate instanceof l0 ? ((l0) longPredicate).a : new k0(longPredicate);
    }

    public final /* synthetic */ m0 a(m0 m0Var) {
        return b(this.a.and(l0.a(m0Var)));
    }

    public final /* synthetic */ m0 c() {
        return b(this.a.negate());
    }

    public final /* synthetic */ m0 d(m0 m0Var) {
        return b(this.a.or(l0.a(m0Var)));
    }

    public final /* synthetic */ boolean e(long j) {
        return this.a.test(j);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof k0) {
            obj = ((k0) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
