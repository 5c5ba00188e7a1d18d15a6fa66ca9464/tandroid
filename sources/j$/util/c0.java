package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
import org.telegram.messenger.LiteMode;
/* loaded from: classes2.dex */
final class c0 implements H {
    private final int[] a;
    private int b;
    private final int c;
    private final int d;

    public c0(int[] iArr, int i, int i2, int i3) {
        this.a = iArr;
        this.b = i;
        this.c = i2;
        this.d = i3 | 64 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM;
    }

    @Override // j$.util.H, j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return a.p(this, consumer);
    }

    @Override // j$.util.N
    /* renamed from: c */
    public final void forEachRemaining(j$.util.function.K k) {
        int i;
        k.getClass();
        int[] iArr = this.a;
        int length = iArr.length;
        int i2 = this.c;
        if (length < i2 || (i = this.b) < 0) {
            return;
        }
        this.b = i2;
        if (i < i2) {
            do {
                k.accept(iArr[i]);
                i++;
            } while (i < i2);
        }
    }

    @Override // j$.util.Q
    public final int characteristics() {
        return this.d;
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        return this.c - this.b;
    }

    @Override // j$.util.H, j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.g(this, consumer);
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

    @Override // j$.util.N
    /* renamed from: j */
    public final boolean tryAdvance(j$.util.function.K k) {
        k.getClass();
        int i = this.b;
        if (i < 0 || i >= this.c) {
            return false;
        }
        this.b = i + 1;
        k.accept(this.a[i]);
        return true;
    }

    @Override // j$.util.Q
    public final H trySplit() {
        int i = this.b;
        int i2 = (this.c + i) >>> 1;
        if (i >= i2) {
            return null;
        }
        this.b = i2;
        return new c0(this.a, i, i2, this.d);
    }
}
