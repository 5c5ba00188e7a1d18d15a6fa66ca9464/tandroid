package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class l0 extends o0 implements k3 {
    final j$.util.function.l b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l0(j$.util.function.l lVar, boolean z) {
        super(z);
        this.b = lVar;
    }

    @Override // j$.util.stream.o0, j$.util.stream.m3
    public void accept(int i) {
        this.b.accept(i);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: e */
    public /* synthetic */ void accept(Integer num) {
        o1.b(this, num);
    }

    @Override // j$.util.function.l
    public j$.util.function.l l(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        return new j$.util.function.k(this, lVar);
    }
}
