package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Comparator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class G2 implements j$.util.Q {
    int a;
    final int b;
    int c;
    final int d;
    Object[] e;
    final /* synthetic */ P2 f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public G2(P2 p2, int i, int i2, int i3, int i4) {
        this.f = p2;
        this.a = i;
        this.b = i2;
        this.c = i3;
        this.d = i4;
        Object[][] objArr = p2.f;
        this.e = objArr == null ? p2.e : objArr[i];
    }

    @Override // j$.util.Q
    public final boolean a(Consumer consumer) {
        consumer.getClass();
        int i = this.a;
        int i2 = this.b;
        if (i < i2 || (i == i2 && this.c < this.d)) {
            Object[] objArr = this.e;
            int i3 = this.c;
            this.c = i3 + 1;
            consumer.accept(objArr[i3]);
            if (this.c == this.e.length) {
                this.c = 0;
                int i4 = this.a + 1;
                this.a = i4;
                Object[][] objArr2 = this.f.f;
                if (objArr2 != null && i4 <= i2) {
                    this.e = objArr2[i4];
                }
            }
            return true;
        }
        return false;
    }

    @Override // j$.util.Q
    public final int characteristics() {
        return 16464;
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        int i = this.a;
        int i2 = this.d;
        int i3 = this.b;
        if (i == i3) {
            return i2 - this.c;
        }
        long[] jArr = this.f.d;
        return ((jArr[i3] + i2) - jArr[i]) - this.c;
    }

    @Override // j$.util.Q
    public final void forEachRemaining(Consumer consumer) {
        P2 p2;
        consumer.getClass();
        int i = this.a;
        int i2 = this.d;
        int i3 = this.b;
        if (i < i3 || (i == i3 && this.c < i2)) {
            int i4 = this.c;
            while (true) {
                p2 = this.f;
                if (i >= i3) {
                    break;
                }
                Object[] objArr = p2.f[i];
                while (i4 < objArr.length) {
                    consumer.accept(objArr[i4]);
                    i4++;
                }
                i++;
                i4 = 0;
            }
            Object[] objArr2 = this.a == i3 ? this.e : p2.f[i3];
            while (i4 < i2) {
                consumer.accept(objArr2[i4]);
                i4++;
            }
            this.a = i3;
            this.c = i2;
        }
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

    @Override // j$.util.Q
    public final j$.util.Q trySplit() {
        int i = this.a;
        int i2 = this.b;
        if (i < i2) {
            int i3 = i2 - 1;
            int i4 = this.c;
            P2 p2 = this.f;
            G2 g2 = new G2(p2, i, i3, i4, p2.f[i3].length);
            this.a = i2;
            this.c = 0;
            this.e = p2.f[i2];
            return g2;
        } else if (i == i2) {
            int i5 = this.c;
            int i6 = (this.d - i5) / 2;
            if (i6 == 0) {
                return null;
            }
            j$.util.Q m = j$.util.f0.m(this.e, i5, i5 + i6);
            this.c += i6;
            return m;
        } else {
            return null;
        }
    }
}
