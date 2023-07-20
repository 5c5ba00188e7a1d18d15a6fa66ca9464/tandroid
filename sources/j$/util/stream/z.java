package j$.util.stream;

import j$.util.function.ToDoubleFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class z implements ToDoubleFunction {
    public static final /* synthetic */ z a = new z();

    private /* synthetic */ z() {
    }

    @Override // j$.util.function.ToDoubleFunction
    public final double applyAsDouble(Object obj) {
        return ((Double) obj).doubleValue();
    }
}
