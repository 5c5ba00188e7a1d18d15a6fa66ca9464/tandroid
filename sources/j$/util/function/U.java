package j$.util.function;

import java.util.function.LongConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class U implements W {
    public final /* synthetic */ LongConsumer a;

    private /* synthetic */ U(LongConsumer longConsumer) {
        this.a = longConsumer;
    }

    public static /* synthetic */ W a(LongConsumer longConsumer) {
        if (longConsumer == null) {
            return null;
        }
        return longConsumer instanceof V ? ((V) longConsumer).a : new U(longConsumer);
    }

    @Override // j$.util.function.W
    public final /* synthetic */ void accept(long j) {
        this.a.accept(j);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        LongConsumer longConsumer = this.a;
        if (obj instanceof U) {
            obj = ((U) obj).a;
        }
        return longConsumer.equals(obj);
    }

    @Override // j$.util.function.W
    public final /* synthetic */ W f(W w) {
        return a(this.a.andThen(V.a(w)));
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
