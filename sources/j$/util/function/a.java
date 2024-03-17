package j$.util.function;

import j$.util.function.Predicate;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class a implements b, Predicate {
    public final /* synthetic */ int a;
    public final /* synthetic */ Object b;

    public /* synthetic */ a(Predicate predicate) {
        this.a = 2;
        this.b = predicate;
    }

    @Override // j$.util.function.Predicate
    public /* synthetic */ Predicate and(Predicate predicate) {
        return Predicate.-CC.$default$and(this, predicate);
    }

    @Override // j$.util.function.BiFunction
    public BiFunction andThen(Function function) {
        switch (this.a) {
            case 0:
                Objects.requireNonNull(function);
                return new j$.util.concurrent.a(this, function);
            default:
                Objects.requireNonNull(function);
                return new j$.util.concurrent.a(this, function);
        }
    }

    @Override // j$.util.function.BiFunction
    public Object apply(Object obj, Object obj2) {
        switch (this.a) {
            case 0:
                return ((Comparator) this.b).compare(obj, obj2) >= 0 ? obj : obj2;
            default:
                return ((Comparator) this.b).compare(obj, obj2) <= 0 ? obj : obj2;
        }
    }

    @Override // j$.util.function.Predicate
    public /* synthetic */ Predicate negate() {
        return Predicate.-CC.$default$negate(this);
    }

    @Override // j$.util.function.Predicate
    public /* synthetic */ Predicate or(Predicate predicate) {
        return Predicate.-CC.$default$or(this, predicate);
    }

    @Override // j$.util.function.Predicate
    public boolean test(Object obj) {
        return !((Predicate) this.b).test(obj);
    }

    public /* synthetic */ a(Comparator comparator, int i) {
        this.a = i;
        if (i != 1) {
            this.b = comparator;
        } else {
            this.b = comparator;
        }
    }
}
