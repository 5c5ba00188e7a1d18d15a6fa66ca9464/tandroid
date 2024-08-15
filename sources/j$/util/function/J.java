package j$.util.function;

import java.util.function.IntConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class J implements IntConsumer {
    public final /* synthetic */ K a;

    private /* synthetic */ J(K k) {
        this.a = k;
    }

    public static /* synthetic */ IntConsumer a(K k) {
        if (k == null) {
            return null;
        }
        return k instanceof I ? ((I) k).a : new J(k);
    }

    @Override // java.util.function.IntConsumer
    public final /* synthetic */ void accept(int i) {
        this.a.accept(i);
    }

    @Override // java.util.function.IntConsumer
    public final /* synthetic */ IntConsumer andThen(IntConsumer intConsumer) {
        return a(this.a.n(I.a(intConsumer)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        K k = this.a;
        if (obj instanceof J) {
            obj = ((J) obj).a;
        }
        return k.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
