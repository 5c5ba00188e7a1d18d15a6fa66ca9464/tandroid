package j$.util.stream;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class z extends A {
    public final /* synthetic */ int l;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ z(b bVar, int i, int i2) {
        super(bVar, i);
        this.l = i2;
    }

    @Override // j$.util.stream.b, j$.util.stream.BaseStream
    public final /* bridge */ /* synthetic */ D parallel() {
        switch (this.l) {
            case 0:
                parallel();
                return this;
            default:
                parallel();
                return this;
        }
    }

    @Override // j$.util.stream.b, j$.util.stream.BaseStream
    public final /* bridge */ /* synthetic */ D sequential() {
        switch (this.l) {
            case 0:
                sequential();
                return this;
            default:
                sequential();
                return this;
        }
    }

    @Override // j$.util.stream.b
    final boolean y0() {
        switch (this.l) {
            case 0:
                return true;
            default:
                return false;
        }
    }
}
