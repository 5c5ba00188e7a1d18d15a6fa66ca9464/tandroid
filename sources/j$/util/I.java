package j$.util;

import j$.util.function.Consumer;
import j$.util.t;
import java.util.Comparator;
import java.util.Objects;
import org.telegram.messenger.LiteMode;
/* loaded from: classes2.dex */
final class I implements t.c {
    private final long[] a;
    private int b;
    private final int c;
    private final int d;

    public I(long[] jArr, int i, int i2, int i3) {
        this.a = jArr;
        this.b = i;
        this.c = i2;
        this.d = i3 | 64 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM;
    }

    @Override // j$.util.t.c, j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return a.l(this, consumer);
    }

    @Override // j$.util.t
    public int characteristics() {
        return this.d;
    }

    @Override // j$.util.u
    /* renamed from: d */
    public void forEachRemaining(j$.util.function.q qVar) {
        int i;
        Objects.requireNonNull(qVar);
        long[] jArr = this.a;
        int length = jArr.length;
        int i2 = this.c;
        if (length < i2 || (i = this.b) < 0) {
            return;
        }
        this.b = i2;
        if (i < i2) {
            do {
                qVar.accept(jArr[i]);
                i++;
            } while (i < i2);
        }
    }

    @Override // j$.util.t
    public long estimateSize() {
        return this.c - this.b;
    }

    @Override // j$.util.t.c, j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.d(this, consumer);
    }

    @Override // j$.util.t
    public Comparator getComparator() {
        if (a.f(this, 4)) {
            return null;
        }
        throw new IllegalStateException();
    }

    @Override // j$.util.t
    public /* synthetic */ long getExactSizeIfKnown() {
        return a.e(this);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return a.f(this, i);
    }

    @Override // j$.util.u
    /* renamed from: i */
    public boolean tryAdvance(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        int i = this.b;
        if (i < 0 || i >= this.c) {
            return false;
        }
        long[] jArr = this.a;
        this.b = i + 1;
        qVar.accept(jArr[i]);
        return true;
    }

    @Override // j$.util.t.c, j$.util.u, j$.util.t
    public t.c trySplit() {
        int i = this.b;
        int i2 = (this.c + i) >>> 1;
        if (i >= i2) {
            return null;
        }
        long[] jArr = this.a;
        this.b = i2;
        return new I(jArr, i, i2, this.d);
    }
}
