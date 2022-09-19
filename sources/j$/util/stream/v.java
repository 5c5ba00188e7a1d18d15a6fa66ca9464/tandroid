package j$.util.stream;
/* loaded from: classes2.dex */
public final /* synthetic */ class v implements j$.util.function.u {
    public static final /* synthetic */ v a = new v();

    private /* synthetic */ v() {
    }

    @Override // j$.util.function.u
    public final void accept(Object obj, double d) {
        double[] dArr = (double[]) obj;
        dArr[2] = dArr[2] + 1.0d;
        l.b(dArr, d);
        dArr[3] = dArr[3] + d;
    }
}
