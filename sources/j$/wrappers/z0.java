package j$.wrappers;

import java.util.function.Supplier;
/* loaded from: classes2.dex */
public final /* synthetic */ class z0 implements Supplier {
    final /* synthetic */ j$.util.function.y a;

    private /* synthetic */ z0(j$.util.function.y yVar) {
        this.a = yVar;
    }

    public static /* synthetic */ Supplier a(j$.util.function.y yVar) {
        if (yVar == null) {
            return null;
        }
        return yVar instanceof y0 ? ((y0) yVar).a : new z0(yVar);
    }

    @Override // java.util.function.Supplier
    public /* synthetic */ Object get() {
        return this.a.get();
    }
}
