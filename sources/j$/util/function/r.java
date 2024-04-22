package j$.util.function;

import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class r implements s {
    public final /* synthetic */ int a;
    public final /* synthetic */ s b;
    public final /* synthetic */ s c;

    public /* synthetic */ r(s sVar, s sVar2, int i) {
        this.a = i;
        if (i != 1) {
            this.b = sVar;
            this.c = sVar2;
            return;
        }
        this.b = sVar;
        this.c = sVar2;
    }

    @Override // j$.util.function.s
    public s a(s sVar) {
        switch (this.a) {
            case 0:
                Objects.requireNonNull(sVar);
                return new r(this, sVar, 0);
            default:
                Objects.requireNonNull(sVar);
                return new r(this, sVar, 0);
        }
    }

    @Override // j$.util.function.s
    public final long applyAsLong(long j) {
        switch (this.a) {
            case 0:
                return this.c.applyAsLong(this.b.applyAsLong(j));
            default:
                return this.b.applyAsLong(this.c.applyAsLong(j));
        }
    }

    @Override // j$.util.function.s
    public s b(s sVar) {
        switch (this.a) {
            case 0:
                Objects.requireNonNull(sVar);
                return new r(this, sVar, 1);
            default:
                Objects.requireNonNull(sVar);
                return new r(this, sVar, 1);
        }
    }
}
