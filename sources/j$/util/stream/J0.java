package j$.util.stream;

import j$.util.Collection$-EL;
import j$.util.function.Consumer;
import java.util.Collection;
import java.util.Iterator;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class J0 implements F0 {
    private final Collection a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public J0(Collection collection) {
        this.a = collection;
    }

    @Override // j$.util.stream.F0
    public final F0 a(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.F0
    public final long count() {
        return this.a.size();
    }

    @Override // j$.util.stream.F0
    public final void forEach(Consumer consumer) {
        Collection$-EL.a(this.a, consumer);
    }

    @Override // j$.util.stream.F0
    public final void i(Object[] objArr, int i) {
        Iterator it = this.a.iterator();
        while (it.hasNext()) {
            objArr[i] = it.next();
            i++;
        }
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ int p() {
        return 0;
    }

    @Override // j$.util.stream.F0
    public final Object[] s(j$.util.function.I i) {
        Collection collection = this.a;
        return collection.toArray((Object[]) i.apply(collection.size()));
    }

    @Override // j$.util.stream.F0
    public final j$.util.Q spliterator() {
        return Collection$-EL.stream(this.a).spliterator();
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ F0 t(long j, long j2, j$.util.function.I i) {
        return t0.w(this, j, j2, i);
    }

    public final String toString() {
        Collection collection = this.a;
        return String.format("CollectionNode[%d][%s]", Integer.valueOf(collection.size()), collection);
    }
}
