package j$.util.stream;

import j$.util.Collection$-EL;
import j$.util.function.Consumer;
import java.util.Collection;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class H0 implements D0 {
    private final Collection a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public H0(Collection collection) {
        this.a = collection;
    }

    @Override // j$.util.stream.D0
    public final D0 a(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.D0
    public final long count() {
        return this.a.size();
    }

    @Override // j$.util.stream.D0
    public final void e(Object[] objArr, int i) {
        for (Object obj : this.a) {
            objArr[i] = obj;
            i++;
        }
    }

    @Override // j$.util.stream.D0
    public final void forEach(Consumer consumer) {
        Collection$-EL.a(this.a, consumer);
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ int j() {
        return 0;
    }

    @Override // j$.util.stream.D0
    public final Object[] o(j$.util.function.N n) {
        Collection collection = this.a;
        return collection.toArray((Object[]) n.apply(collection.size()));
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ D0 q(long j, long j2, j$.util.function.N n) {
        return u0.B0(this, j, j2, n);
    }

    @Override // j$.util.stream.D0
    public final j$.util.Q spliterator() {
        return Collection$-EL.stream(this.a).spliterator();
    }

    public final String toString() {
        Collection collection = this.a;
        return String.format("CollectionNode[%d][%s]", Integer.valueOf(collection.size()), collection);
    }
}
