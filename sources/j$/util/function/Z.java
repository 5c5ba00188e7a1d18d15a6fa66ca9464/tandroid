package j$.util.function;

import java.util.function.LongPredicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class Z {
    public final /* synthetic */ LongPredicate a;

    private /* synthetic */ Z(LongPredicate longPredicate) {
        this.a = longPredicate;
    }

    public static /* synthetic */ Z a(LongPredicate longPredicate) {
        if (longPredicate == null) {
            return null;
        }
        return new Z(longPredicate);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        LongPredicate longPredicate = this.a;
        if (obj instanceof Z) {
            obj = ((Z) obj).a;
        }
        return longPredicate.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
