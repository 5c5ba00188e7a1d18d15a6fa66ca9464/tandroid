package j$.util.stream;

import j$.util.Optional;
import j$.util.function.Predicate;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class V implements Predicate {
    public static final /* synthetic */ V a = new V();

    private /* synthetic */ V() {
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
        return ((Optional) obj).isPresent();
    }
}
