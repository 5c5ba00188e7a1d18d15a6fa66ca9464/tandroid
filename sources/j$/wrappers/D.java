package j$.wrappers;

import java.util.function.DoublePredicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class D {
    final /* synthetic */ DoublePredicate a;

    private /* synthetic */ D(DoublePredicate doublePredicate) {
        this.a = doublePredicate;
    }

    public static /* synthetic */ D a(DoublePredicate doublePredicate) {
        if (doublePredicate == null) {
            return null;
        }
        return doublePredicate instanceof E ? ((E) doublePredicate).a : new D(doublePredicate);
    }

    public boolean b(double d) {
        return this.a.test(d);
    }
}
