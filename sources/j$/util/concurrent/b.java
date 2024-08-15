package j$.util.concurrent;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class b extends q {
    final ConcurrentHashMap i;
    m j;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(m[] mVarArr, int i, int i2, ConcurrentHashMap concurrentHashMap) {
        super(mVarArr, i, 0, i2);
        this.i = concurrentHashMap;
        f();
    }

    public final boolean hasMoreElements() {
        return this.b != null;
    }

    public final boolean hasNext() {
        return this.b != null;
    }

    public final void remove() {
        m mVar = this.j;
        if (mVar == null) {
            throw new IllegalStateException();
        }
        this.j = null;
        this.i.i(mVar.b, null, null);
    }
}
