package j$.util.function;
/* loaded from: classes2.dex */
public interface Consumer<T> {

    /* loaded from: classes2.dex */
    public final /* synthetic */ class -CC {
    }

    void accept(T t);

    Consumer<T> andThen(Consumer<? super T> consumer);
}
