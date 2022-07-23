package j$.wrappers;

import java.util.function.LongPredicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class j0 {
    final /* synthetic */ LongPredicate a;

    private /* synthetic */ j0(LongPredicate longPredicate) {
        this.a = longPredicate;
    }

    public static /* synthetic */ j0 a(LongPredicate longPredicate) {
        if (longPredicate == null) {
            return null;
        }
        return longPredicate instanceof k0 ? ((k0) longPredicate).a : new j0(longPredicate);
    }

    public boolean b(long j) {
        return this.a.test(j);
    }
}
