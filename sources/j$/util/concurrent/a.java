package j$.util.concurrent;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class a extends o {
    final ConcurrentHashMap i;
    k j;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(k[] kVarArr, int i, int i2, ConcurrentHashMap concurrentHashMap) {
        super(kVarArr, i, 0, i2);
        this.i = concurrentHashMap;
        b();
    }

    public final boolean hasMoreElements() {
        return this.b != null;
    }

    public final boolean hasNext() {
        return this.b != null;
    }

    public final void remove() {
        k kVar = this.j;
        if (kVar == null) {
            throw new IllegalStateException();
        }
        this.j = null;
        this.i.h(kVar.b, null, null);
    }
}
