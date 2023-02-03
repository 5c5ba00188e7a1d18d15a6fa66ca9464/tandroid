package org.telegram.messenger;

import j$.util.function.Function;
import java.util.Objects;
import org.telegram.messenger.TranslateController;
/* loaded from: classes.dex */
public final /* synthetic */ class TranslateController$$ExternalSyntheticLambda12 implements Function {
    public static final /* synthetic */ TranslateController$$ExternalSyntheticLambda12 INSTANCE = new TranslateController$$ExternalSyntheticLambda12();

    private /* synthetic */ TranslateController$$ExternalSyntheticLambda12() {
    }

    @Override // j$.util.function.Function
    public /* synthetic */ Function andThen(Function function) {
        return Objects.requireNonNull(function);
    }

    @Override // j$.util.function.Function
    public final Object apply(Object obj) {
        String str;
        str = ((TranslateController.Language) obj).displayName;
        return str;
    }

    @Override // j$.util.function.Function
    public /* synthetic */ Function compose(Function function) {
        return Objects.requireNonNull(function);
    }
}
