package j$.util.stream;

import j$.util.DesugarArrays;
import j$.util.function.Consumer;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
class S3 implements j$.util.s {
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

    @Override // j$.util.s
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

    @Override // j$.util.s
    public int characteristics() {
        return 16464;
    }

    @Override // j$.util.s
    public long estimateSize() {
        int i = this.a;
        int i2 = this.b;
        if (i == i2) {
            return this.d - this.c;
        }
        long[] jArr = this.f.d;
        return ((jArr[i2] + this.d) - jArr[i]) - this.c;
    }

    @Override // j$.util.s
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

    @Override // j$.util.s
    public Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.s
    public /* synthetic */ long getExactSizeIfKnown() {
        return j$.util.a.e(this);
    }

    @Override // j$.util.s
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.f(this, i);
    }

    @Override // j$.util.s
    public j$.util.s trySplit() {
        int i = this.a;
        int i2 = this.b;
        if (i < i2) {
            a4 a4Var = this.f;
            S3 s3 = new S3(a4Var, i, i2 - 1, this.c, a4Var.f[i2 - 1].length);
            int i3 = this.b;
            this.a = i3;
            this.c = 0;
            this.e = this.f.f[i3];
            return s3;
        } else if (i == i2) {
            int i4 = this.d;
            int i5 = this.c;
            int i6 = (i4 - i5) / 2;
            if (i6 == 0) {
                return null;
            }
            j$.util.s a = DesugarArrays.a(this.e, i5, i5 + i6);
            this.c += i6;
            return a;
        } else {
            return null;
        }
    }
}
