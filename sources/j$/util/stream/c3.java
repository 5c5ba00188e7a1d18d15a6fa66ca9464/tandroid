package j$.util.stream;

import j$.util.concurrent.ConcurrentHashMap;
import j$.util.function.Consumer;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class c3 implements j$.util.Q, Consumer {
    private static final Object d = new Object();
    private final j$.util.Q a;
    private final ConcurrentHashMap b;
    private Object c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c3(j$.util.Q q) {
        this(q, new ConcurrentHashMap());
    }

    private c3(j$.util.Q q, ConcurrentHashMap concurrentHashMap) {
        this.a = q;
        this.b = concurrentHashMap;
    }

    @Override // j$.util.Q
    public final boolean a(Consumer consumer) {
        while (this.a.a(this)) {
            Object obj = this.c;
            if (obj == null) {
                obj = d;
            }
            if (this.b.putIfAbsent(obj, Boolean.TRUE) == null) {
                consumer.accept(this.c);
                this.c = null;
                return true;
            }
        }
        return false;
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
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

    @Override // j$.util.Q
    public final void forEachRemaining(Consumer consumer) {
        this.a.forEachRemaining(new n(6, this, consumer));
    }

    @Override // j$.util.Q
    public final Comparator getComparator() {
        return this.a.getComparator();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return j$.util.a.i(this);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.k(this, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void i(Consumer consumer, Object obj) {
        if (this.b.putIfAbsent(obj != null ? obj : d, Boolean.TRUE) == null) {
            consumer.accept(obj);
        }
    }

    @Override // j$.util.Q
    public final j$.util.Q trySplit() {
        j$.util.Q trySplit = this.a.trySplit();
        if (trySplit != null) {
            return new c3(trySplit, this.b);
        }
        return null;
    }
}
