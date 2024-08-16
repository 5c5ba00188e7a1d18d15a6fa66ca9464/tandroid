package j$.util.concurrent;

import j$.util.H;
import j$.util.function.Consumer;
import j$.util.function.F;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class y implements H {
    long a;
    final long b;
    final int c;
    final int d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y(long j, long j2, int i, int i2) {
        this.a = j;
        this.b = j2;
        this.c = i;
        this.d = i2;
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        j$.util.a.f(this, consumer);
    }

    @Override // j$.util.Q
    /* renamed from: b */
    public final y trySplit() {
        long j = this.a;
        long j2 = (this.b + j) >>> 1;
        if (j2 <= j) {
            return null;
        }
        this.a = j2;
        return new y(j, j2, this.c, this.d);
    }

    @Override // j$.util.N
    /* renamed from: c */
    public final void forEachRemaining(F f) {
        f.getClass();
        long j = this.a;
        long j2 = this.b;
        if (j < j2) {
            this.a = j2;
            ThreadLocalRandom current = ThreadLocalRandom.current();
            do {
                f.accept(current.d(this.c, this.d));
                j++;
            } while (j < j2);
        }
    }

    @Override // j$.util.Q
    public final int characteristics() {
        return 17728;
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        return this.b - this.a;
    }

    @Override // j$.util.N
    /* renamed from: g */
    public final boolean tryAdvance(F f) {
        f.getClass();
        long j = this.a;
        if (j < this.b) {
            f.accept(ThreadLocalRandom.current().d(this.c, this.d));
            this.a = j + 1;
            return true;
        }
        return false;
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
    public final /* synthetic */ boolean s(Consumer consumer) {
        return j$.util.a.o(this, consumer);
    }
}
