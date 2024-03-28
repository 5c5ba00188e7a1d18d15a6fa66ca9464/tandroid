package j$.wrappers;

import j$.util.function.Supplier;
/* loaded from: classes2.dex */
public final /* synthetic */ class y0 implements Supplier {
    final /* synthetic */ java.util.function.Supplier a;

    private /* synthetic */ y0(java.util.function.Supplier supplier) {
        this.a = supplier;
    }

    public static /* synthetic */ Supplier a(java.util.function.Supplier supplier) {
        if (supplier == null) {
            return null;
        }
        return supplier instanceof z0 ? ((z0) supplier).a : new y0(supplier);
    }

    @Override // j$.util.function.Supplier
    public /* synthetic */ Object get() {
        return this.a.get();
    }
}
