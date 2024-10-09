package j$.util.function;

/* loaded from: classes2.dex */
public final /* synthetic */ class u0 implements java.util.function.Supplier {
    public final /* synthetic */ Supplier a;

    private /* synthetic */ u0(Supplier supplier) {
        this.a = supplier;
    }

    public static /* synthetic */ java.util.function.Supplier a(Supplier supplier) {
        if (supplier == null) {
            return null;
        }
        return supplier instanceof t0 ? ((t0) supplier).a : new u0(supplier);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        Supplier supplier = this.a;
        if (obj instanceof u0) {
            obj = ((u0) obj).a;
        }
        return supplier.equals(obj);
    }

    @Override // java.util.function.Supplier
    public final /* synthetic */ Object get() {
        return this.a.get();
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
