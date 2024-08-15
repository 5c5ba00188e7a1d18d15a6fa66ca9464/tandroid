package j$.util.function;
/* loaded from: classes2.dex */
public final /* synthetic */ class H implements K {
    public final /* synthetic */ K a;
    public final /* synthetic */ K b;

    public /* synthetic */ H(K k, K k2) {
        this.a = k;
        this.b = k2;
    }

    @Override // j$.util.function.K
    public final void accept(int i) {
        this.a.accept(i);
        this.b.accept(i);
    }

    @Override // j$.util.function.K
    public final K n(K k) {
        k.getClass();
        return new H(this, k);
    }
}
