package org.telegram.ui;

import j$.util.function.Function;
import java.util.Objects;
import org.telegram.ui.CountrySelectActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class NewContactBottomSheet$$ExternalSyntheticLambda10 implements Function {
    public static final /* synthetic */ NewContactBottomSheet$$ExternalSyntheticLambda10 INSTANCE = new NewContactBottomSheet$$ExternalSyntheticLambda10();

    private /* synthetic */ NewContactBottomSheet$$ExternalSyntheticLambda10() {
    }

    @Override // j$.util.function.Function
    public /* synthetic */ Function andThen(Function function) {
        return Objects.requireNonNull(function);
    }

    @Override // j$.util.function.Function
    public final Object apply(Object obj) {
        String str;
        str = ((CountrySelectActivity.Country) obj).name;
        return str;
    }

    @Override // j$.util.function.Function
    public /* synthetic */ Function compose(Function function) {
        return Objects.requireNonNull(function);
    }
}
