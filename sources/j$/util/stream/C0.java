package j$.util.stream;

import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class C0 implements j$.util.function.l {
    public final /* synthetic */ int a = 1;
    public final /* synthetic */ Object b;

    @Override // j$.util.function.l
    public final void accept(int i) {
        switch (this.a) {
            case 0:
                ((m3) this.b).accept(i);
                return;
            default:
                ((G0) this.b).a.accept(i);
                return;
        }
    }

    @Override // j$.util.function.l
    public j$.util.function.l l(j$.util.function.l lVar) {
        switch (this.a) {
            case 0:
                Objects.requireNonNull(lVar);
                return new j$.util.function.k(this, lVar);
            default:
                Objects.requireNonNull(lVar);
                return new j$.util.function.k(this, lVar);
        }
    }
}
