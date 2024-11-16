package j$.util.function;

import java.util.Comparator;

/* loaded from: classes2.dex */
public final /* synthetic */ class c implements f {
    public final /* synthetic */ int a;
    public final /* synthetic */ Comparator b;

    public /* synthetic */ c(Comparator comparator, int i) {
        this.a = i;
        this.b = comparator;
    }

    @Override // j$.util.function.BiFunction
    public final /* synthetic */ BiFunction andThen(Function function) {
        switch (this.a) {
        }
        return j$.com.android.tools.r8.a.a(this, function);
    }

    @Override // j$.util.function.BiFunction
    public final Object apply(Object obj, Object obj2) {
        switch (this.a) {
            case 0:
                if (this.b.compare(obj, obj2) < 0) {
                    break;
                }
                break;
            default:
                if (this.b.compare(obj, obj2) > 0) {
                    break;
                }
                break;
        }
        return obj2;
    }
}
