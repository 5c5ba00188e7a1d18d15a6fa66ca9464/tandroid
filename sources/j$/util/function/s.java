package j$.util.function;

import java.util.function.DoubleToIntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class s {
    public final /* synthetic */ DoubleToIntFunction a;

    private /* synthetic */ s(DoubleToIntFunction doubleToIntFunction) {
        this.a = doubleToIntFunction;
    }

    public static /* synthetic */ s a(DoubleToIntFunction doubleToIntFunction) {
        if (doubleToIntFunction == null) {
            return null;
        }
        return new s(doubleToIntFunction);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        DoubleToIntFunction doubleToIntFunction = this.a;
        if (obj instanceof s) {
            obj = ((s) obj).a;
        }
        return doubleToIntFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
