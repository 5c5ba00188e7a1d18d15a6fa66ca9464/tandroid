package j$.util.stream;
/* loaded from: classes2.dex */
abstract class C1 implements A1 {
    protected final A1 a;
    protected final A1 b;
    private final long c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public C1(A1 a1, A1 a12) {
        this.a = a1;
        this.b = a12;
        this.c = a1.count() + a12.count();
    }

    @Override // j$.util.stream.A1
    public A1 b(int i) {
        if (i == 0) {
            return this.a;
        }
        if (i == 1) {
            return this.b;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.A1
    public /* bridge */ /* synthetic */ z1 b(int i) {
        return (z1) b(i);
    }

    @Override // j$.util.stream.A1
    public long count() {
        return this.c;
    }

    @Override // j$.util.stream.A1
    public int p() {
        return 2;
    }
}
