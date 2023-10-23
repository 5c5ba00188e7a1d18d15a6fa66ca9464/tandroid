package j$.util.function;
/* loaded from: classes2.dex */
public interface BiConsumer<T, U> {

    /* loaded from: classes2.dex */
    public final /* synthetic */ class -CC {
    }

    void accept(T t, U u);

    BiConsumer<T, U> andThen(BiConsumer<? super T, ? super U> biConsumer);
}
