package j$.util.concurrent;

import j$.util.Q;
import j$.util.function.Consumer;
import java.util.Comparator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class k extends q implements Q {
    public final /* synthetic */ int i;
    long j;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ k(m[] mVarArr, int i, int i2, int i3, long j, int i4) {
        super(mVarArr, i, i2, i3);
        this.i = i4;
        this.j = j;
    }

    @Override // j$.util.Q
    public final boolean a(Consumer consumer) {
        switch (this.i) {
            case 0:
                consumer.getClass();
                m f = f();
                if (f == null) {
                    return false;
                }
                consumer.accept(f.b);
                return true;
            default:
                consumer.getClass();
                m f2 = f();
                if (f2 == null) {
                    return false;
                }
                consumer.accept(f2.c);
                return true;
        }
    }

    @Override // j$.util.Q
    public final int characteristics() {
        switch (this.i) {
            case 0:
                return 4353;
            default:
                return 4352;
        }
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        switch (this.i) {
            case 0:
                return this.j;
            default:
                return this.j;
        }
    }

    @Override // j$.util.Q
    public final void forEachRemaining(Consumer consumer) {
        switch (this.i) {
            case 0:
                consumer.getClass();
                while (true) {
                    m f = f();
                    if (f == null) {
                        return;
                    }
                    consumer.accept(f.b);
                }
            default:
                consumer.getClass();
                while (true) {
                    m f2 = f();
                    if (f2 == null) {
                        return;
                    }
                    consumer.accept(f2.c);
                }
        }
    }

    @Override // j$.util.Q
    public final Comparator getComparator() {
        switch (this.i) {
            case 0:
                throw new IllegalStateException();
            default:
                throw new IllegalStateException();
        }
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        switch (this.i) {
            case 0:
                return j$.util.a.i(this);
            default:
                return j$.util.a.i(this);
        }
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        switch (this.i) {
            case 0:
                return j$.util.a.k(this, i);
            default:
                return j$.util.a.k(this, i);
        }
    }

    @Override // j$.util.Q
    public final Q trySplit() {
        switch (this.i) {
            case 0:
                int i = this.f;
                int i2 = this.g;
                int i3 = (i + i2) >>> 1;
                if (i3 <= i) {
                    return null;
                }
                m[] mVarArr = this.a;
                int i4 = this.h;
                this.g = i3;
                long j = this.j >>> 1;
                this.j = j;
                return new k(mVarArr, i4, i3, i2, j, 0);
            default:
                int i5 = this.f;
                int i6 = this.g;
                int i7 = (i5 + i6) >>> 1;
                if (i7 <= i5) {
                    return null;
                }
                m[] mVarArr2 = this.a;
                int i8 = this.h;
                this.g = i7;
                long j2 = this.j >>> 1;
                this.j = j2;
                return new k(mVarArr2, i8, i7, i6, j2, 1);
        }
    }
}
