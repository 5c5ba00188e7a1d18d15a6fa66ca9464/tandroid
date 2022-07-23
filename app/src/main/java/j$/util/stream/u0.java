package j$.util.stream;
/* loaded from: classes2.dex */
public final /* synthetic */ class u0 implements j$.util.function.v {
    public static final /* synthetic */ u0 a = new u0();

    private /* synthetic */ u0() {
    }

    @Override // j$.util.function.v
    public final void accept(Object obj, int i) {
        long[] jArr = (long[]) obj;
        jArr[0] = jArr[0] + 1;
        jArr[1] = jArr[1] + i;
    }
}
