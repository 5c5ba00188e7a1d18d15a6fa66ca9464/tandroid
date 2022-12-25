package j$.wrappers;

import java.util.function.IntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class T implements IntFunction {
    final /* synthetic */ j$.util.function.m a;

    private /* synthetic */ T(j$.util.function.m mVar) {
        this.a = mVar;
    }

    public static /* synthetic */ IntFunction a(j$.util.function.m mVar) {
        if (mVar == null) {
            return null;
        }
        return mVar instanceof S ? ((S) mVar).a : new T(mVar);
    }

    @Override // java.util.function.IntFunction
    public /* synthetic */ Object apply(int i) {
        return this.a.apply(i);
    }
}
