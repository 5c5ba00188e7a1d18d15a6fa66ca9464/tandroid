package j$.util.function;
/* loaded from: classes2.dex */
public interface Predicate<T> {

    /* loaded from: classes2.dex */
    public final /* synthetic */ class -CC {
        public static Predicate $default$negate(Predicate predicate) {
            return new a(predicate);
        }
    }

    Predicate<T> and(Predicate<? super T> predicate);

    Predicate<T> negate();

    Predicate<T> or(Predicate<? super T> predicate);

    boolean test(T t);
}
