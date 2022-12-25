package j$.wrappers;

import java.util.function.LongBinaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class d0 implements LongBinaryOperator {
    final /* synthetic */ j$.util.function.o a;

    private /* synthetic */ d0(j$.util.function.o oVar) {
        this.a = oVar;
    }

    public static /* synthetic */ LongBinaryOperator a(j$.util.function.o oVar) {
        if (oVar == null) {
            return null;
        }
        return oVar instanceof c0 ? ((c0) oVar).a : new d0(oVar);
    }

    @Override // java.util.function.LongBinaryOperator
    public /* synthetic */ long applyAsLong(long j, long j2) {
        return this.a.applyAsLong(j, j2);
    }
}
