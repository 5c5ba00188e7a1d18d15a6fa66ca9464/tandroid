package j$.util.function;

import java.util.function.IntBinaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class A implements IntBinaryOperator {
    public final /* synthetic */ B a;

    private /* synthetic */ A(B b) {
        this.a = b;
    }

    public static /* synthetic */ IntBinaryOperator a(B b) {
        if (b == null) {
            return null;
        }
        return b instanceof z ? ((z) b).a : new A(b);
    }

    @Override // java.util.function.IntBinaryOperator
    public final /* synthetic */ int applyAsInt(int i, int i2) {
        return this.a.applyAsInt(i, i2);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        B b = this.a;
        if (obj instanceof A) {
            obj = ((A) obj).a;
        }
        return b.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
