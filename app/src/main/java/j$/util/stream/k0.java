package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class k0 extends o0 implements j3 {
    final j$.util.function.f b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k0(j$.util.function.f fVar, boolean z) {
        super(z);
        this.b = fVar;
    }

    @Override // j$.util.stream.o0, j$.util.stream.m3
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
        fVar.getClass();
        return new j$.util.function.e(this, fVar);
    }
}
