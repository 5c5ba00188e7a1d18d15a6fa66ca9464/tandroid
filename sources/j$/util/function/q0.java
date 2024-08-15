package j$.util.function;

import java.util.function.LongToIntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class q0 implements s0 {
    public final /* synthetic */ LongToIntFunction a;

    private /* synthetic */ q0(LongToIntFunction longToIntFunction) {
        this.a = longToIntFunction;
    }

    public static /* synthetic */ s0 b(LongToIntFunction longToIntFunction) {
        if (longToIntFunction == null) {
            return null;
        }
        return longToIntFunction instanceof r0 ? ((r0) longToIntFunction).a : new q0(longToIntFunction);
    }

    public final /* synthetic */ int a(long j) {
        return this.a.applyAsInt(j);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof q0) {
            obj = ((q0) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
