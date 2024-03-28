package j$.wrappers;

import java.util.function.Supplier;
/* loaded from: classes2.dex */
public final /* synthetic */ class z0 implements Supplier {
    final /* synthetic */ j$.util.function.Supplier a;

    private /* synthetic */ z0(j$.util.function.Supplier supplier) {
        this.a = supplier;
    }

    public static /* synthetic */ Supplier a(j$.util.function.Supplier supplier) {
        if (supplier == null) {
            return null;
        }
        return supplier instanceof y0 ? ((y0) supplier).a : new z0(supplier);
    }

    @Override // java.util.function.Supplier
    public /* synthetic */ Object get() {
        return this.a.get();
    }
}
