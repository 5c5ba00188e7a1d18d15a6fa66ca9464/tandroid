package j$.util.function;

import java.util.function.IntPredicate;

/* loaded from: classes2.dex */
public final /* synthetic */ class J {
    public final /* synthetic */ IntPredicate a;

    private /* synthetic */ J(IntPredicate intPredicate) {
        this.a = intPredicate;
    }

    public static /* synthetic */ J a(IntPredicate intPredicate) {
        if (intPredicate == null) {
            return null;
        }
        return new J(intPredicate);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        IntPredicate intPredicate = this.a;
        if (obj instanceof J) {
            obj = ((J) obj).a;
        }
        return intPredicate.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
