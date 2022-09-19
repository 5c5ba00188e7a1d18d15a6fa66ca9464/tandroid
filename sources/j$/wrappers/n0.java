package j$.wrappers;

import java.util.function.LongToIntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class n0 {
    final /* synthetic */ LongToIntFunction a;

    private /* synthetic */ n0(LongToIntFunction longToIntFunction) {
        this.a = longToIntFunction;
    }

    public static /* synthetic */ n0 b(LongToIntFunction longToIntFunction) {
        if (longToIntFunction == null) {
            return null;
        }
        return longToIntFunction instanceof o0 ? ((o0) longToIntFunction).a : new n0(longToIntFunction);
    }

    public int a(long j) {
        return this.a.applyAsInt(j);
    }
}
