package j$.util.stream;
/* loaded from: classes2.dex */
abstract class D1 implements B1 {
    protected final B1 a;
    protected final B1 b;
    private final long c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public D1(B1 b1, B1 b12) {
        this.a = b1;
        this.b = b12;
        this.c = b1.count() + b12.count();
    }

    @Override // j$.util.stream.B1
    public /* bridge */ /* synthetic */ A1 b(int i) {
        return (A1) b(i);
    }

    @Override // j$.util.stream.B1
    public B1 b(int i) {
        if (i == 0) {
            return this.a;
        }
        if (i == 1) {
            return this.b;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.B1
    public long count() {
        return this.c;
    }

    @Override // j$.util.stream.B1
    public int p() {
        return 2;
    }
}
