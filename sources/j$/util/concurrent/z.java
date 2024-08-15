package j$.util.concurrent;

import j$.util.E;
import j$.util.function.Consumer;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class z implements E {
    long a;
    final long b;
    final double c;
    final double d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public z(long j, long j2, double d, double d2) {
        this.a = j;
        this.b = j2;
        this.c = d;
        this.d = d2;
    }

    @Override // j$.util.E, j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.n(this, consumer);
    }

    @Override // j$.util.Q
    public final int characteristics() {
        return 17728;
    }

    @Override // j$.util.N
    /* renamed from: d */
    public final void forEachRemaining(j$.util.function.m mVar) {
        mVar.getClass();
        long j = this.a;
        long j2 = this.b;
        if (j < j2) {
            this.a = j2;
            ThreadLocalRandom current = ThreadLocalRandom.current();
            do {
                mVar.accept(current.c(this.c, this.d));
                j++;
            } while (j < j2);
        }
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        return this.b - this.a;
    }

    @Override // j$.util.Q
    /* renamed from: f */
    public final z trySplit() {
        long j = this.a;
        long j2 = (this.b + j) >>> 1;
        if (j2 <= j) {
            return null;
        }
        this.a = j2;
        return new z(j, j2, this.c, this.d);
    }

    @Override // j$.util.E, j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.f(this, consumer);
    }

    @Override // j$.util.Q
    public final Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return j$.util.a.i(this);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.k(this, i);
    }

    @Override // j$.util.N
    /* renamed from: o */
    public final boolean tryAdvance(j$.util.function.m mVar) {
        mVar.getClass();
        long j = this.a;
        if (j < this.b) {
            mVar.accept(ThreadLocalRandom.current().c(this.c, this.d));
            this.a = j + 1;
            return true;
        }
        return false;
    }
}
