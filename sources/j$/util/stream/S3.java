package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Comparator;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class S3 implements j$.util.u {
    int a;
    final int b;
    int c;
    final int d;
    Object[] e;
    final /* synthetic */ a4 f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public S3(a4 a4Var, int i, int i2, int i3, int i4) {
        this.f = a4Var;
        this.a = i;
        this.b = i2;
        this.c = i3;
        this.d = i4;
        Object[][] objArr = a4Var.f;
        this.e = objArr == null ? a4Var.e : objArr[i];
    }

    @Override // j$.util.u
    public boolean b(Consumer consumer) {
        Objects.requireNonNull(consumer);
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
                if (objArr2 != null && i4 <= this.b) {
                    this.e = objArr2[i4];
                }
            }
            return true;
        }
        return false;
    }

    @Override // j$.util.u
    public int characteristics() {
        return 16464;
    }

    @Override // j$.util.u
    public long estimateSize() {
        int i = this.a;
        int i2 = this.b;
        if (i == i2) {
            return this.d - this.c;
        }
        long[] jArr = this.f.d;
        return ((jArr[i2] + this.d) - jArr[i]) - this.c;
    }

    @Override // j$.util.u
    public void forEachRemaining(Consumer consumer) {
        int i;
        Objects.requireNonNull(consumer);
        int i2 = this.a;
        int i3 = this.b;
        if (i2 < i3 || (i2 == i3 && this.c < this.d)) {
            int i4 = this.c;
            while (true) {
                i = this.b;
                if (i2 >= i) {
                    break;
                }
                Object[] objArr = this.f.f[i2];
                while (i4 < objArr.length) {
                    consumer.accept(objArr[i4]);
                    i4++;
                }
                i4 = 0;
                i2++;
            }
            Object[] objArr2 = this.a == i ? this.e : this.f.f[i];
            int i5 = this.d;
            while (i4 < i5) {
                consumer.accept(objArr2[i4]);
                i4++;
            }
            this.a = this.b;
            this.c = this.d;
        }
    }

    @Override // j$.util.u
    public Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.u
    public /* synthetic */ long getExactSizeIfKnown() {
        return j$.util.a.e(this);
    }

    @Override // j$.util.u
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.f(this, i);
    }

    @Override // j$.util.u
    public j$.util.u trySplit() {
        int i = this.a;
        int i2 = this.b;
        if (i < i2) {
            a4 a4Var = this.f;
            int i3 = i2 - 1;
            S3 s3 = new S3(a4Var, i, i3, this.c, a4Var.f[i3].length);
            int i4 = this.b;
            this.a = i4;
            this.c = 0;
            this.e = this.f.f[i4];
            return s3;
        } else if (i == i2) {
            int i5 = this.d;
            int i6 = this.c;
            int i7 = (i5 - i6) / 2;
            if (i7 == 0) {
                return null;
            }
            j$.util.u m = j$.util.L.m(this.e, i6, i6 + i7, 1040);
            this.c += i7;
            return m;
        } else {
            return null;
        }
    }
}
