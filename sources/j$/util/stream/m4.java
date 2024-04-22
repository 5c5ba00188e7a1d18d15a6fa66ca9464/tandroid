package j$.util.stream;

import j$.util.concurrent.ConcurrentHashMap;
import j$.util.function.Consumer;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class m4 implements j$.util.s, Consumer {
    private static final Object d = new Object();
    private final j$.util.s a;
    private final ConcurrentHashMap b;
    private Object c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m4(j$.util.s sVar) {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        this.a = sVar;
        this.b = concurrentHashMap;
    }

    private m4(j$.util.s sVar, ConcurrentHashMap concurrentHashMap) {
        this.a = sVar;
        this.b = concurrentHashMap;
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        this.c = obj;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.s
    public boolean b(Consumer consumer) {
        while (this.a.b(this)) {
            ConcurrentHashMap concurrentHashMap = this.b;
            Object obj = this.c;
            if (obj == null) {
                obj = d;
            }
            if (concurrentHashMap.putIfAbsent(obj, Boolean.TRUE) == null) {
                consumer.accept(this.c);
                this.c = null;
                return true;
            }
        }
        return false;
    }

    @Override // j$.util.s
    public int characteristics() {
        return (this.a.characteristics() & (-16469)) | 1;
    }

    @Override // j$.util.s
    public long estimateSize() {
        return this.a.estimateSize();
    }

    public void f(Consumer consumer, Object obj) {
        if (this.b.putIfAbsent(obj != null ? obj : d, Boolean.TRUE) == null) {
            consumer.accept(obj);
        }
    }

    @Override // j$.util.s
    public void forEachRemaining(Consumer consumer) {
        this.a.forEachRemaining(new p(this, consumer));
    }

    @Override // j$.util.s
    public Comparator getComparator() {
        return this.a.getComparator();
    }

    @Override // j$.util.s
    public /* synthetic */ long getExactSizeIfKnown() {
        return j$.util.a.e(this);
    }

    @Override // j$.util.s
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.f(this, i);
    }

    @Override // j$.util.s
    public j$.util.s trySplit() {
        j$.util.s trySplit = this.a.trySplit();
        if (trySplit != null) {
            return new m4(trySplit, this.b);
        }
        return null;
    }
}
