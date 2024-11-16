package j$.util;

import j$.util.function.Consumer;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import org.telegram.tgnet.ConnectionsManager;

/* loaded from: classes2.dex */
class d0 implements Q {
    private final Collection a;
    private Iterator b = null;
    private final int c;
    private long d;
    private int e;

    public d0(Collection collection, int i) {
        this.a = collection;
        this.c = i | 16448;
    }

    @Override // j$.util.Q
    public final void a(Consumer consumer) {
        consumer.getClass();
        Iterator it = this.b;
        if (it == null) {
            Iterator it2 = this.a.iterator();
            this.b = it2;
            this.d = r0.size();
            it = it2;
        }
        if (it instanceof j) {
            ((j) it).a(consumer);
        } else {
            while (it.hasNext()) {
                consumer.accept(it.next());
            }
        }
    }

    @Override // j$.util.Q
    public final int characteristics() {
        return this.c;
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        if (this.b != null) {
            return this.d;
        }
        Collection collection = this.a;
        this.b = collection.iterator();
        long size = collection.size();
        this.d = size;
        return size;
    }

    @Override // j$.util.Q
    public Comparator getComparator() {
        if (a.k(this, 4)) {
            return null;
        }
        throw new IllegalStateException();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return a.j(this);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return a.k(this, i);
    }

    @Override // j$.util.Q
    public final boolean s(Consumer consumer) {
        consumer.getClass();
        if (this.b == null) {
            this.b = this.a.iterator();
            this.d = r0.size();
        }
        if (!this.b.hasNext()) {
            return false;
        }
        consumer.accept(this.b.next());
        return true;
    }

    @Override // j$.util.Q
    public final Q trySplit() {
        long j;
        Iterator it = this.b;
        if (it == null) {
            Collection collection = this.a;
            Iterator it2 = collection.iterator();
            this.b = it2;
            j = collection.size();
            this.d = j;
            it = it2;
        } else {
            j = this.d;
        }
        if (j <= 1 || !it.hasNext()) {
            return null;
        }
        int i = this.e + 1024;
        if (i > j) {
            i = (int) j;
        }
        if (i > 33554432) {
            i = ConnectionsManager.FileTypeVideo;
        }
        Object[] objArr = new Object[i];
        int i2 = 0;
        do {
            objArr[i2] = it.next();
            i2++;
            if (i2 >= i) {
                break;
            }
        } while (it.hasNext());
        this.e = i2;
        long j2 = this.d;
        if (j2 != Long.MAX_VALUE) {
            this.d = j2 - i2;
        }
        return new W(objArr, 0, i2, this.c);
    }
}
