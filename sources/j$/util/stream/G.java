package j$.util.stream;

import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class G implements j$.util.function.f {
    public final /* synthetic */ int a = 1;
    public final /* synthetic */ Object b;

    @Override // j$.util.function.f
    public final void accept(double d) {
        switch (this.a) {
            case 0:
                ((m3) this.b).accept(d);
                return;
            default:
                ((K) this.b).a.accept(d);
                return;
        }
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        switch (this.a) {
            case 0:
                Objects.requireNonNull(fVar);
                return new j$.util.function.e(this, fVar);
            default:
                Objects.requireNonNull(fVar);
                return new j$.util.function.e(this, fVar);
        }
    }
}
