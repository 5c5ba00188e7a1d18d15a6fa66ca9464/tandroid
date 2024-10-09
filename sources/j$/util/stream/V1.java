package j$.util.stream;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class V1 extends W1 {
    public final /* synthetic */ int l;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ V1(b bVar, int i, int i2) {
        super(bVar, i);
        this.l = i2;
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
