package j$.util.concurrent;

import j$.util.Q;
import j$.util.function.Consumer;
import java.util.Comparator;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class i extends o implements Q {
    public final /* synthetic */ int i;
    long j;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ i(k[] kVarArr, int i, int i2, int i3, long j, int i4) {
        super(kVarArr, i, i2, i3);
        this.i = i4;
        this.j = j;
    }

    @Override // j$.util.Q
    public final void a(Consumer consumer) {
        switch (this.i) {
            case 0:
                consumer.getClass();
                while (true) {
                    k b = b();
                    if (b == null) {
                        return;
                    } else {
                        consumer.r(b.b);
                    }
                }
            default:
                consumer.getClass();
                while (true) {
                    k b2 = b();
                    if (b2 == null) {
                        return;
                    } else {
                        consumer.r(b2.c);
                    }
                }
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
                return j$.util.a.j(this);
            default:
                return j$.util.a.j(this);
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
    public final boolean s(Consumer consumer) {
        switch (this.i) {
            case 0:
                consumer.getClass();
                k b = b();
                if (b == null) {
                    return false;
                }
                consumer.r(b.b);
                return true;
            default:
                consumer.getClass();
                k b2 = b();
                if (b2 == null) {
                    return false;
                }
                consumer.r(b2.c);
                return true;
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
                k[] kVarArr = this.a;
                this.g = i3;
                long j = this.j >>> 1;
                this.j = j;
                return new i(kVarArr, this.h, i3, i2, j, 0);
            default:
                int i4 = this.f;
                int i5 = this.g;
                int i6 = (i4 + i5) >>> 1;
                if (i6 <= i4) {
                    return null;
                }
                k[] kVarArr2 = this.a;
                this.g = i6;
                long j2 = this.j >>> 1;
                this.j = j2;
                return new i(kVarArr2, this.h, i6, i5, j2, 1);
        }
    }
}
