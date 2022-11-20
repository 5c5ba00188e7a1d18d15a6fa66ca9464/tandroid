package j$.util.stream;

import j$.util.function.Predicate;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class Y implements Predicate {
    public static final /* synthetic */ Y a = new Y();

    private /* synthetic */ Y() {
    }

    @Override // j$.util.function.Predicate
    public /* synthetic */ Predicate and(Predicate predicate) {
        return Objects.requireNonNull(predicate);
    }

    @Override // j$.util.function.Predicate
    public /* synthetic */ Predicate negate() {
        return Predicate.-CC.$default$negate(this);
    }

    @Override // j$.util.function.Predicate
    public /* synthetic */ Predicate or(Predicate predicate) {
        return Objects.requireNonNull(predicate);
    }

    @Override // j$.util.function.Predicate
    public final boolean test(Object obj) {
        return ((j$.util.l) obj).c();
    }
}
