package j$.util.function;

import j$.util.function.Predicate;
/* loaded from: classes2.dex */
public interface Predicate<T> {

    /* loaded from: classes2.dex */
    public final /* synthetic */ class -CC<T> {
        public static Predicate $default$and(Predicate predicate, Predicate predicate2) {
            predicate2.getClass();
            return new G0(predicate, predicate2, 0);
        }

        public static Predicate $default$negate(final Predicate predicate) {
            return new Predicate() { // from class: j$.util.function.H0
                @Override // j$.util.function.Predicate
                public final /* synthetic */ Predicate and(Predicate predicate2) {
                    return Predicate.-CC.$default$and(this, predicate2);
                }

                @Override // j$.util.function.Predicate
                public final /* synthetic */ Predicate negate() {
                    return Predicate.-CC.$default$negate(this);
                }

                @Override // j$.util.function.Predicate
                public final /* synthetic */ Predicate or(Predicate predicate2) {
                    return Predicate.-CC.$default$or(this, predicate2);
                }

                @Override // j$.util.function.Predicate
                public final boolean test(Object obj) {
                    return !Predicate.this.test(obj);
                }
            };
        }

        public static Predicate $default$or(Predicate predicate, Predicate predicate2) {
            predicate2.getClass();
            return new G0(predicate, predicate2, 1);
        }
    }

    Predicate<T> and(Predicate<? super T> predicate);

    Predicate<T> negate();

    Predicate<T> or(Predicate<? super T> predicate);

    boolean test(T t);
}
