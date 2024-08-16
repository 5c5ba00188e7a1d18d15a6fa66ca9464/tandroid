package j$.util.stream;
/* loaded from: classes2.dex */
abstract class d {
    protected final int a;
    protected int b;
    protected int c;
    protected long[] d;

    /* JADX INFO: Access modifiers changed from: protected */
    public d() {
        this.a = 4;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public d(int i) {
        if (i >= 0) {
            this.a = Math.max(4, 32 - Integer.numberOfLeadingZeros(i - 1));
            return;
        }
        throw new IllegalArgumentException("Illegal Capacity: " + i);
    }

    public abstract void clear();

    public final long count() {
        int i = this.c;
        return i == 0 ? this.b : this.d[i] + this.b;
    }
}
