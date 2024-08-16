package j$.util.function;

import java.util.function.LongToIntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class b0 {
    public final /* synthetic */ LongToIntFunction a;

    private /* synthetic */ b0(LongToIntFunction longToIntFunction) {
        this.a = longToIntFunction;
    }

    public static /* synthetic */ b0 a(LongToIntFunction longToIntFunction) {
        if (longToIntFunction == null) {
            return null;
        }
        return new b0(longToIntFunction);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        LongToIntFunction longToIntFunction = this.a;
        if (obj instanceof b0) {
            obj = ((b0) obj).a;
        }
        return longToIntFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
