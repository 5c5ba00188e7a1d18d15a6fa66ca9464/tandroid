package j$.wrappers;

import java.util.function.IntPredicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class U {
    final /* synthetic */ IntPredicate a;

    private /* synthetic */ U(IntPredicate intPredicate) {
        this.a = intPredicate;
    }

    public static /* synthetic */ U a(IntPredicate intPredicate) {
        if (intPredicate == null) {
            return null;
        }
        return intPredicate instanceof V ? ((V) intPredicate).a : new U(intPredicate);
    }

    public boolean b(int i) {
        return this.a.test(i);
    }
}
