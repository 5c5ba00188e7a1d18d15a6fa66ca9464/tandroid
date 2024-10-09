package j$.util.stream;

/* loaded from: classes2.dex */
abstract class H0 implements F0 {
    protected final F0 a;
    protected final F0 b;
    private final long c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public H0(F0 f0, F0 f02) {
        this.a = f0;
        this.b = f02;
        this.c = f0.count() + f02.count();
    }

    @Override // j$.util.stream.F0
    public /* bridge */ /* synthetic */ E0 a(int i) {
        return (E0) a(i);
    }

    @Override // j$.util.stream.F0
    public final F0 a(int i) {
        if (i == 0) {
            return this.a;
        }
        if (i == 1) {
            return this.b;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.F0
    public final long count() {
        return this.c;
    }

    @Override // j$.util.stream.F0
    public final int p() {
        return 2;
    }
}
