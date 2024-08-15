package j$.util.stream;
/* loaded from: classes2.dex */
abstract class F0 implements D0 {
    protected final D0 a;
    protected final D0 b;
    private final long c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public F0(D0 d0, D0 d02) {
        this.a = d0;
        this.b = d02;
        this.c = d0.count() + d02.count();
    }

    @Override // j$.util.stream.D0
    public /* bridge */ /* synthetic */ C0 a(int i) {
        return (C0) a(i);
    }

    @Override // j$.util.stream.D0
    public final D0 a(int i) {
        if (i == 0) {
            return this.a;
        }
        if (i == 1) {
            return this.b;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.D0
    public final long count() {
        return this.c;
    }

    @Override // j$.util.stream.D0
    public final int j() {
        return 2;
    }
}
