package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class m0 extends o0 implements l3 {
    final j$.util.function.q b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m0(j$.util.function.q qVar, boolean z) {
        super(z);
        this.b = qVar;
    }

    @Override // j$.util.stream.o0, j$.util.stream.m3, j$.util.stream.l3, j$.util.function.q
    public void accept(long j) {
        this.b.accept(j);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: e */
    public /* synthetic */ void accept(Long l) {
        o1.c(this, l);
    }

    @Override // j$.util.function.q
    public j$.util.function.q f(j$.util.function.q qVar) {
        qVar.getClass();
        return new j$.util.function.p(this, qVar);
    }
}
