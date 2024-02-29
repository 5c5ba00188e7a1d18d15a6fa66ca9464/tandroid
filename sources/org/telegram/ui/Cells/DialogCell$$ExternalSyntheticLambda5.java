package org.telegram.ui.Cells;

import j$.util.function.ToIntFunction;
import org.telegram.messenger.MessageObject;
/* loaded from: classes4.dex */
public final /* synthetic */ class DialogCell$$ExternalSyntheticLambda5 implements ToIntFunction {
    public static final /* synthetic */ DialogCell$$ExternalSyntheticLambda5 INSTANCE = new DialogCell$$ExternalSyntheticLambda5();

    private /* synthetic */ DialogCell$$ExternalSyntheticLambda5() {
    }

    @Override // j$.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        return ((MessageObject) obj).getId();
    }
}
