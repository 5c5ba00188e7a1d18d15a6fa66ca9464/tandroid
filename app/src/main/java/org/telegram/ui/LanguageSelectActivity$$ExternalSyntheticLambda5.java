package org.telegram.ui;

import j$.util.function.Predicate;
/* loaded from: classes3.dex */
public final /* synthetic */ class LanguageSelectActivity$$ExternalSyntheticLambda5 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ LanguageSelectActivity$$ExternalSyntheticLambda5(String str) {
        this.f$0 = str;
    }

    @Override // j$.util.function.Predicate
    public /* synthetic */ Predicate and(Predicate predicate) {
        return predicate.getClass();
    }

    @Override // j$.util.function.Predicate
    public /* synthetic */ Predicate negate() {
        return Predicate.CC.$default$negate(this);
    }

    @Override // j$.util.function.Predicate
    public /* synthetic */ Predicate or(Predicate predicate) {
        return predicate.getClass();
    }

    @Override // j$.util.function.Predicate
    public final boolean test(Object obj) {
        boolean lambda$createView$0;
        lambda$createView$0 = LanguageSelectActivity.lambda$createView$0(this.f$0, (String) obj);
        return lambda$createView$0;
    }
}
