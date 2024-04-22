package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class l0 extends p0 implements j3 {
    final j$.util.function.f b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l0(j$.util.function.f fVar, boolean z) {
        super(z);
        this.b = fVar;
    }

    @Override // j$.util.stream.p0, j$.util.stream.m3
    public void accept(double d) {
        this.b.accept(d);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: e */
    public /* synthetic */ void accept(Double d) {
        o1.a(this, d);
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return new j$.util.function.e(this, fVar);
    }
}
