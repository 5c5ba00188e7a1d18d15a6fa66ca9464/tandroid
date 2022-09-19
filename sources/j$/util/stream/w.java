package j$.util.stream;
/* loaded from: classes2.dex */
public final /* synthetic */ class w implements j$.util.function.u {
    public static final /* synthetic */ w a = new w();

    private /* synthetic */ w() {
    }

    @Override // j$.util.function.u
    public final void accept(Object obj, double d) {
        double[] dArr = (double[]) obj;
        l.b(dArr, d);
        dArr[2] = dArr[2] + d;
    }
}
