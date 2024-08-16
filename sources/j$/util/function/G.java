package j$.util.function;

import java.util.function.IntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class G implements I {
    public final /* synthetic */ IntFunction a;

    private /* synthetic */ G(IntFunction intFunction) {
        this.a = intFunction;
    }

    public static /* synthetic */ I a(IntFunction intFunction) {
        if (intFunction == null) {
            return null;
        }
        return intFunction instanceof H ? ((H) intFunction).a : new G(intFunction);
    }

    @Override // j$.util.function.I
    public final /* synthetic */ Object apply(int i) {
        return this.a.apply(i);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        IntFunction intFunction = this.a;
        if (obj instanceof G) {
            obj = ((G) obj).a;
        }
        return intFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
