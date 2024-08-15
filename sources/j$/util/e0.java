package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
import org.telegram.messenger.LiteMode;
/* loaded from: classes2.dex */
final class e0 implements K {
    private final long[] a;
    private int b;
    private final int c;
    private final int d;

    public e0(long[] jArr, int i, int i2, int i3) {
        this.a = jArr;
        this.b = i;
        this.c = i2;
        this.d = i3 | 64 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM;
    }

    @Override // j$.util.K, j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return a.q(this, consumer);
    }

    @Override // j$.util.N
    /* renamed from: b */
    public final void forEachRemaining(j$.util.function.h0 h0Var) {
        int i;
        h0Var.getClass();
        long[] jArr = this.a;
        int length = jArr.length;
        int i2 = this.c;
        if (length < i2 || (i = this.b) < 0) {
            return;
        }
        this.b = i2;
        if (i < i2) {
            do {
                h0Var.accept(jArr[i]);
                i++;
            } while (i < i2);
        }
    }

    @Override // j$.util.Q
    public final int characteristics() {
        return this.d;
    }

    @Override // j$.util.N
    /* renamed from: e */
    public final boolean tryAdvance(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        int i = this.b;
        if (i < 0 || i >= this.c) {
            return false;
        }
        this.b = i + 1;
        h0Var.accept(this.a[i]);
        return true;
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        return this.c - this.b;
    }

    @Override // j$.util.K, j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.h(this, consumer);
    }

    @Override // j$.util.Q
    public final Comparator getComparator() {
        if (a.k(this, 4)) {
            return null;
        }
        throw new IllegalStateException();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return a.i(this);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return a.k(this, i);
    }

    @Override // j$.util.Q
    public final K trySplit() {
        int i = this.b;
        int i2 = (this.c + i) >>> 1;
        if (i >= i2) {
            return null;
        }
        this.b = i2;
        return new e0(this.a, i, i2, this.d);
    }
}
