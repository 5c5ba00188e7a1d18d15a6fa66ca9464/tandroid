package j$.util.stream;

/* loaded from: classes2.dex */
final class O extends Q implements d2 {
    final j$.util.function.W b;

    O(j$.util.function.W w, boolean z) {
        super(z);
        this.b = w;
    }

    @Override // j$.util.stream.Q, j$.util.stream.e2
    public final void accept(long j) {
        this.b.accept(j);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public final /* bridge */ /* synthetic */ void r(Object obj) {
        j((Long) obj);
    }

    @Override // j$.util.function.W
    public final /* synthetic */ j$.util.function.W f(j$.util.function.W w) {
        return j$.com.android.tools.r8.a.d(this, w);
    }

    @Override // j$.util.stream.d2
    public final /* synthetic */ void j(Long l) {
        t0.i(this, l);
    }
}
