package j$.wrappers;

import java.util.function.LongPredicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class i0 {
    final /* synthetic */ LongPredicate a;

    private /* synthetic */ i0(LongPredicate longPredicate) {
        this.a = longPredicate;
    }

    public static /* synthetic */ i0 a(LongPredicate longPredicate) {
        if (longPredicate == null) {
            return null;
        }
        return longPredicate instanceof j0 ? ((j0) longPredicate).a : new i0(longPredicate);
    }

    public boolean b(long j) {
        return this.a.test(j);
    }
}
