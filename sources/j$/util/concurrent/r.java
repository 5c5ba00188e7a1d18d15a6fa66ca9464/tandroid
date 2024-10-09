package j$.util.concurrent;

import j$.util.Collection$-EL;
import j$.util.P;
import j$.util.Q;
import j$.util.function.Consumer;
import j$.util.function.G;
import j$.util.function.Predicate;
import j$.util.function.r0;
import j$.util.stream.P2;
import j$.util.stream.t0;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class r extends b implements j$.util.b {
    /* JADX INFO: Access modifiers changed from: package-private */
    public r(ConcurrentHashMap concurrentHashMap) {
        super(concurrentHashMap);
    }

    @Override // j$.util.b
    public final /* synthetic */ boolean a(Predicate predicate) {
        return j$.util.a.l(this, predicate);
    }

    @Override // java.util.Collection
    public final boolean add(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public final boolean addAll(Collection collection) {
        throw new UnsupportedOperationException();
    }

    @Override // j$.util.concurrent.b, java.util.Collection
    public final boolean contains(Object obj) {
        return this.a.containsValue(obj);
    }

    @Override // j$.util.b
    public final void forEach(Consumer consumer) {
        consumer.getClass();
        k[] kVarArr = this.a.a;
        if (kVarArr == null) {
            return;
        }
        o oVar = new o(kVarArr, kVarArr.length, 0, kVarArr.length);
        while (true) {
            k b = oVar.b();
            if (b == null) {
                return;
            } else {
                consumer.r(b.c);
            }
        }
    }

    @Override // java.lang.Iterable
    public final /* synthetic */ void forEach(java.util.function.Consumer consumer) {
        forEach(j$.util.function.g.a(consumer));
    }

    @Override // j$.util.concurrent.b, java.util.Collection, java.lang.Iterable
    public final Iterator iterator() {
        ConcurrentHashMap concurrentHashMap = this.a;
        k[] kVarArr = concurrentHashMap.a;
        int length = kVarArr == null ? 0 : kVarArr.length;
        return new g(kVarArr, length, length, concurrentHashMap, 1);
    }

    @Override // java.util.Collection
    public final Stream parallelStream() {
        return P2.i0(t0.e0(Collection$-EL.b(this), true));
    }

    @Override // java.util.Collection
    public final boolean remove(Object obj) {
        a aVar;
        if (obj == null) {
            return false;
        }
        Object it = iterator();
        do {
            aVar = (a) it;
            if (!aVar.hasNext()) {
                return false;
            }
        } while (!obj.equals(((g) it).next()));
        aVar.remove();
        return true;
    }

    @Override // java.util.Collection
    public final /* synthetic */ boolean removeIf(java.util.function.Predicate predicate) {
        return j$.util.a.l(this, r0.a(predicate));
    }

    @Override // java.util.Collection, java.lang.Iterable, j$.util.b, java.util.Set
    public final Q spliterator() {
        ConcurrentHashMap concurrentHashMap = this.a;
        long k = concurrentHashMap.k();
        k[] kVarArr = concurrentHashMap.a;
        int length = kVarArr == null ? 0 : kVarArr.length;
        return new i(kVarArr, length, 0, length, k < 0 ? 0L : k, 1);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public final /* synthetic */ Spliterator spliterator() {
        return P.a(spliterator());
    }

    @Override // java.util.Collection, j$.util.b
    public final /* synthetic */ j$.util.stream.Stream stream() {
        return j$.util.a.m(this);
    }

    @Override // java.util.Collection
    public final /* synthetic */ Stream stream() {
        return P2.i0(j$.util.a.m(this));
    }

    @Override // java.util.Collection
    public final Object[] toArray(IntFunction intFunction) {
        return toArray((Object[]) G.a(intFunction).apply(0));
    }
}
