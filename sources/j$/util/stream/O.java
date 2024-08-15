package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class O extends S implements d2 {
    final j$.util.function.K b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public O(j$.util.function.K k, boolean z) {
        super(z);
        this.b = k;
    }

    @Override // j$.util.stream.S, j$.util.stream.f2
    public final void accept(int i) {
        this.b.accept(i);
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        g((Integer) obj);
    }

    @Override // j$.util.stream.d2
    public final /* synthetic */ void g(Integer num) {
        u0.l0(this, num);
    }

    @Override // j$.util.function.K
    public final j$.util.function.K n(j$.util.function.K k) {
        k.getClass();
        return new j$.util.function.H(this, k);
    }
}
