package j$.wrappers;

import j$.util.function.Consumer;
import java.util.PrimitiveIterator;
/* loaded from: classes2.dex */
public final /* synthetic */ class a implements j$.util.n {
    final /* synthetic */ PrimitiveIterator.OfDouble a;

    private /* synthetic */ a(PrimitiveIterator.OfDouble ofDouble) {
        this.a = ofDouble;
    }

    public static /* synthetic */ j$.util.n a(PrimitiveIterator.OfDouble ofDouble) {
        if (ofDouble == null) {
            return null;
        }
        return ofDouble instanceof b ? ((b) ofDouble).a : new a(ofDouble);
    }

    @Override // j$.util.n
    public /* synthetic */ void e(j$.util.function.f fVar) {
        this.a.forEachRemaining(B.a(fVar));
    }

    @Override // j$.util.n, j$.util.Iterator
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        this.a.forEachRemaining(x.a(consumer));
    }

    @Override // j$.util.p
    public /* synthetic */ void forEachRemaining(Object obj) {
        this.a.forEachRemaining((PrimitiveIterator.OfDouble) obj);
    }

    @Override // java.util.Iterator
    public /* synthetic */ boolean hasNext() {
        return this.a.hasNext();
    }

    @Override // j$.util.n, java.util.Iterator, j$.util.Iterator
    /* renamed from: next */
    public /* synthetic */ Double mo335next() {
        return this.a.next();
    }

    @Override // j$.util.n, java.util.Iterator, j$.util.Iterator
    /* renamed from: next  reason: collision with other method in class */
    public /* synthetic */ Object mo335next() {
        return this.a.next();
    }

    @Override // j$.util.n
    public /* synthetic */ double nextDouble() {
        return this.a.nextDouble();
    }

    @Override // java.util.Iterator
    public /* synthetic */ void remove() {
        this.a.remove();
    }
}
