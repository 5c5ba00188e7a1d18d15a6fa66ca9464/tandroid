package j$.util.concurrent;

import j$.util.Q;
import j$.util.function.Consumer;
import java.util.Comparator;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class e extends o implements Q {
    final ConcurrentHashMap i;
    long j;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(k[] kVarArr, int i, int i2, int i3, long j, ConcurrentHashMap concurrentHashMap) {
        super(kVarArr, i, i2, i3);
        this.i = concurrentHashMap;
        this.j = j;
    }

    @Override // j$.util.Q
    public final void a(Consumer consumer) {
        consumer.getClass();
        while (true) {
            k b = b();
            if (b == null) {
                return;
            } else {
                consumer.r(new j(b.b, b.c, this.i));
            }
        }
    }

    @Override // j$.util.Q
    public final int characteristics() {
        return 4353;
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        return this.j;
    }

    @Override // j$.util.Q
    public final Comparator getComparator() {
        throw new IllegalStateException();
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
        consumer.getClass();
        k b = b();
        if (b == null) {
            return false;
        }
        consumer.r(new j(b.b, b.c, this.i));
        return true;
    }

    @Override // j$.util.Q
    public final Q trySplit() {
        int i = this.f;
        int i2 = this.g;
        int i3 = (i + i2) >>> 1;
        if (i3 <= i) {
            return null;
        }
        k[] kVarArr = this.a;
        this.g = i3;
        long j = this.j >>> 1;
        this.j = j;
        return new e(kVarArr, this.h, i3, i2, j, this.i);
    }
}
