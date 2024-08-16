package j$.util.function;

import java.util.function.LongBinaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class Q implements LongBinaryOperator {
    public final /* synthetic */ S a;

    private /* synthetic */ Q(S s) {
        this.a = s;
    }

    public static /* synthetic */ LongBinaryOperator a(S s) {
        if (s == null) {
            return null;
        }
        return s instanceof P ? ((P) s).a : new Q(s);
    }

    @Override // java.util.function.LongBinaryOperator
    public final /* synthetic */ long applyAsLong(long j, long j2) {
        return this.a.applyAsLong(j, j2);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        S s = this.a;
        if (obj instanceof Q) {
            obj = ((Q) obj).a;
        }
        return s.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
