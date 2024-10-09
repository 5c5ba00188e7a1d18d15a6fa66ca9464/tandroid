package j$.util.stream;

import j$.util.concurrent.ConcurrentHashMap;
import j$.util.function.Consumer;
import java.util.Comparator;

/* loaded from: classes2.dex */
final class b3 implements j$.util.Q, Consumer {
    private static final Object d = new Object();
    private final j$.util.Q a;
    private final ConcurrentHashMap b;
    private Object c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b3(j$.util.Q q) {
        this(q, new ConcurrentHashMap());
    }

    private b3(j$.util.Q q, ConcurrentHashMap concurrentHashMap) {
        this.a = q;
        this.b = concurrentHashMap;
    }

    @Override // j$.util.Q
    public final void a(Consumer consumer) {
        this.a.a(new k0(4, this, consumer));
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public final void r(Object obj) {
        this.c = obj;
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.Q
    public final int characteristics() {
        return (this.a.characteristics() & (-16469)) | 1;
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        return this.a.estimateSize();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void f(Consumer consumer, Object obj) {
        if (this.b.putIfAbsent(obj != null ? obj : d, Boolean.TRUE) == null) {
            consumer.r(obj);
        }
    }

    @Override // j$.util.Q
    public final Comparator getComparator() {
        return this.a.getComparator();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return j$.util.a.j(this);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.k(this, i);
    }

    @Override // j$.util.Q
    public final boolean s(Consumer consumer) {
        while (this.a.s(this)) {
            Object obj = this.c;
            if (obj == null) {
                obj = d;
            }
            if (this.b.putIfAbsent(obj, Boolean.TRUE) == null) {
                consumer.r(this.c);
                this.c = null;
                return true;
            }
        }
        return false;
    }

    @Override // j$.util.Q
    public final j$.util.Q trySplit() {
        j$.util.Q trySplit = this.a.trySplit();
        if (trySplit != null) {
            return new b3(trySplit, this.b);
        }
        return null;
    }
}
