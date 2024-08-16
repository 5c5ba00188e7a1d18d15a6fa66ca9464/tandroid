package j$.util.function;

import java.util.function.IntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class H implements IntFunction {
    public final /* synthetic */ I a;

    private /* synthetic */ H(I i) {
        this.a = i;
    }

    public static /* synthetic */ IntFunction a(I i) {
        if (i == null) {
            return null;
        }
        return i instanceof G ? ((G) i).a : new H(i);
    }

    @Override // java.util.function.IntFunction
    public final /* synthetic */ Object apply(int i) {
        return this.a.apply(i);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        I i = this.a;
        if (obj instanceof H) {
            obj = ((H) obj).a;
        }
        return i.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
