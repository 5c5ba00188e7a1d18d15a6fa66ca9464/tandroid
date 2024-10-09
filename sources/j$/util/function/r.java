package j$.util.function;

import java.util.function.DoublePredicate;

/* loaded from: classes2.dex */
public final /* synthetic */ class r {
    public final /* synthetic */ DoublePredicate a;

    private /* synthetic */ r(DoublePredicate doublePredicate) {
        this.a = doublePredicate;
    }

    public static /* synthetic */ r a(DoublePredicate doublePredicate) {
        if (doublePredicate == null) {
            return null;
        }
        return new r(doublePredicate);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        DoublePredicate doublePredicate = this.a;
        if (obj instanceof r) {
            obj = ((r) obj).a;
        }
        return doublePredicate.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
