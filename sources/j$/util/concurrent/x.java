package j$.util.concurrent;

import j$.util.E;
import j$.util.function.Consumer;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class x implements E {
    long a;
    final long b;
    final double c;
    final double d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public x(long j, long j2, double d, double d2) {
        this.a = j;
        this.b = j2;
        this.c = d;
        this.d = d2;
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        j$.util.a.b(this, consumer);
    }

    @Override // j$.util.Q
    /* renamed from: b */
    public final x trySplit() {
        long j = this.a;
        long j2 = (this.b + j) >>> 1;
        if (j2 <= j) {
            return null;
        }
        this.a = j2;
        return new x(j, j2, this.c, this.d);
    }

    @Override // j$.util.Q
    public final int characteristics() {
        return 17728;
    }

    @Override // j$.util.N
    /* renamed from: e */
    public final void forEachRemaining(j$.util.function.n nVar) {
        nVar.getClass();
        long j = this.a;
        long j2 = this.b;
        if (j < j2) {
            this.a = j2;
            ThreadLocalRandom current = ThreadLocalRandom.current();
            do {
                nVar.accept(current.c(this.c, this.d));
                j++;
            } while (j < j2);
        }
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        return this.b - this.a;
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

    @Override // j$.util.N
    /* renamed from: p */
    public final boolean tryAdvance(j$.util.function.n nVar) {
        nVar.getClass();
        long j = this.a;
        if (j < this.b) {
            nVar.accept(ThreadLocalRandom.current().c(this.c, this.d));
            this.a = j + 1;
            return true;
        }
        return false;
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return j$.util.a.n(this, consumer);
    }
}
