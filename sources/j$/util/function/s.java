package j$.util.function;

import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class s implements t {
    public final /* synthetic */ int a;
    public final /* synthetic */ t b;
    public final /* synthetic */ t c;

    public /* synthetic */ s(t tVar, t tVar2, int i) {
        this.a = i;
        if (i != 1) {
            this.b = tVar;
            this.c = tVar2;
            return;
        }
        this.b = tVar;
        this.c = tVar2;
    }

    @Override // j$.util.function.t
    public t a(t tVar) {
        switch (this.a) {
            case 0:
                Objects.requireNonNull(tVar);
                return new s(this, tVar, 0);
            default:
                Objects.requireNonNull(tVar);
                return new s(this, tVar, 0);
        }
    }

    @Override // j$.util.function.t
    public final long applyAsLong(long j) {
        switch (this.a) {
            case 0:
                return this.c.applyAsLong(this.b.applyAsLong(j));
            default:
                return this.b.applyAsLong(this.c.applyAsLong(j));
        }
    }

    @Override // j$.util.function.t
    public t b(t tVar) {
        switch (this.a) {
            case 0:
                Objects.requireNonNull(tVar);
                return new s(this, tVar, 1);
            default:
                Objects.requireNonNull(tVar);
                return new s(this, tVar, 1);
        }
    }
}
