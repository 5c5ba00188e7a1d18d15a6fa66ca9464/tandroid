package j$.util.function;

import java.util.function.IntConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class E implements IntConsumer {
    public final /* synthetic */ F a;

    private /* synthetic */ E(F f) {
        this.a = f;
    }

    public static /* synthetic */ IntConsumer a(F f) {
        if (f == null) {
            return null;
        }
        return f instanceof D ? ((D) f).a : new E(f);
    }

    @Override // java.util.function.IntConsumer
    public final /* synthetic */ void accept(int i) {
        this.a.accept(i);
    }

    @Override // java.util.function.IntConsumer
    public final /* synthetic */ IntConsumer andThen(IntConsumer intConsumer) {
        return a(this.a.l(D.a(intConsumer)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        F f = this.a;
        if (obj instanceof E) {
            obj = ((E) obj).a;
        }
        return f.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
