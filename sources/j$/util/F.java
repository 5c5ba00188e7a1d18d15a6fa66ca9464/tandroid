package j$.util;

import j$.util.function.Consumer;
import j$.util.s;
import java.util.Comparator;
import java.util.Objects;
import org.telegram.messenger.LiteMode;
/* loaded from: classes2.dex */
final class F implements s.b {
    private final int[] a;
    private int b;
    private final int c;
    private final int d;

    public F(int[] iArr, int i, int i2, int i3) {
        this.a = iArr;
        this.b = i;
        this.c = i2;
        this.d = i3 | 64 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM;
    }

    @Override // j$.util.s.b, j$.util.s
    public /* synthetic */ boolean b(Consumer consumer) {
        return a.k(this, consumer);
    }

    @Override // j$.util.t
    /* renamed from: c */
    public void forEachRemaining(j$.util.function.l lVar) {
        int i;
        Objects.requireNonNull(lVar);
        int[] iArr = this.a;
        int length = iArr.length;
        int i2 = this.c;
        if (length < i2 || (i = this.b) < 0) {
            return;
        }
        this.b = i2;
        if (i < i2) {
            do {
                lVar.accept(iArr[i]);
                i++;
            } while (i < i2);
        }
    }

    @Override // j$.util.s
    public int characteristics() {
        return this.d;
    }

    @Override // j$.util.s
    public long estimateSize() {
        return this.c - this.b;
    }

    @Override // j$.util.s.b, j$.util.s
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.c(this, consumer);
    }

    @Override // j$.util.t
    /* renamed from: g */
    public boolean tryAdvance(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        int i = this.b;
        if (i < 0 || i >= this.c) {
            return false;
        }
        int[] iArr = this.a;
        this.b = i + 1;
        lVar.accept(iArr[i]);
        return true;
    }

    @Override // j$.util.s
    public Comparator getComparator() {
        if (a.f(this, 4)) {
            return null;
        }
        throw new IllegalStateException();
    }

    @Override // j$.util.s
    public /* synthetic */ long getExactSizeIfKnown() {
        return a.e(this);
    }

    @Override // j$.util.s
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return a.f(this, i);
    }

    @Override // j$.util.t, j$.util.s
    public s.b trySplit() {
        int i = this.b;
        int i2 = (this.c + i) >>> 1;
        if (i >= i2) {
            return null;
        }
        int[] iArr = this.a;
        this.b = i2;
        return new F(iArr, i, i2, this.d);
    }
}
