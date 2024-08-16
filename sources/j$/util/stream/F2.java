package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Comparator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class F2 implements j$.util.Q {
    int a;
    final int b;
    int c;
    final int d;
    Object[] e;
    final /* synthetic */ O2 f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public F2(O2 o2, int i, int i2, int i3, int i4) {
        this.f = o2;
        this.a = i;
        this.b = i2;
        this.c = i3;
        this.d = i4;
        Object[][] objArr = o2.f;
        this.e = objArr == null ? o2.e : objArr[i];
    }

    @Override // j$.util.Q
    public final void a(Consumer consumer) {
        O2 o2;
        consumer.getClass();
        int i = this.a;
        int i2 = this.d;
        int i3 = this.b;
        if (i < i3 || (i == i3 && this.c < i2)) {
            int i4 = this.c;
            while (true) {
                o2 = this.f;
                if (i >= i3) {
                    break;
                }
                Object[] objArr = o2.f[i];
                while (i4 < objArr.length) {
                    consumer.accept(objArr[i4]);
                    i4++;
                }
                i++;
                i4 = 0;
            }
            Object[] objArr2 = this.a == i3 ? this.e : o2.f[i3];
            while (i4 < i2) {
                consumer.accept(objArr2[i4]);
                i4++;
            }
            this.a = i3;
            this.c = i2;
        }
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
    public final j$.util.Q trySplit() {
        int i = this.a;
        int i2 = this.b;
        if (i < i2) {
            int i3 = i2 - 1;
            int i4 = this.c;
            O2 o2 = this.f;
            F2 f2 = new F2(o2, i, i3, i4, o2.f[i3].length);
            this.a = i2;
            this.c = 0;
            this.e = o2.f[i2];
            return f2;
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
