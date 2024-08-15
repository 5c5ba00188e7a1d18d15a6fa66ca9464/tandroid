package j$.util.stream;
/* loaded from: classes2.dex */
final class X2 extends Z2 implements j$.util.function.K {
    final int[] c = new int[128];

    @Override // j$.util.stream.Z2
    public final void a(Object obj, long j) {
        j$.util.function.K k = (j$.util.function.K) obj;
        for (int i = 0; i < j; i++) {
            k.accept(this.c[i]);
        }
    }

    @Override // j$.util.function.K
    public final void accept(int i) {
        int i2 = this.b;
        this.b = i2 + 1;
        this.c[i2] = i;
    }

    @Override // j$.util.function.K
    public final j$.util.function.K n(j$.util.function.K k) {
        k.getClass();
        return new j$.util.function.H(this, k);
    }
}
