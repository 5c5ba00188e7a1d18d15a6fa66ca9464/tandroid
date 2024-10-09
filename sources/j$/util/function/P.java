package j$.util.function;

import java.util.function.LongBinaryOperator;

/* loaded from: classes2.dex */
public final /* synthetic */ class P implements S {
    public final /* synthetic */ LongBinaryOperator a;

    private /* synthetic */ P(LongBinaryOperator longBinaryOperator) {
        this.a = longBinaryOperator;
    }

    public static /* synthetic */ S a(LongBinaryOperator longBinaryOperator) {
        if (longBinaryOperator == null) {
            return null;
        }
        return longBinaryOperator instanceof Q ? ((Q) longBinaryOperator).a : new P(longBinaryOperator);
    }

    @Override // j$.util.function.S
    public final /* synthetic */ long applyAsLong(long j, long j2) {
        return this.a.applyAsLong(j, j2);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        LongBinaryOperator longBinaryOperator = this.a;
        if (obj instanceof P) {
            obj = ((P) obj).a;
        }
        return longBinaryOperator.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
