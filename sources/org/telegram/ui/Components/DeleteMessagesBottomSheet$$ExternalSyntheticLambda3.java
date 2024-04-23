package org.telegram.ui.Components;

import j$.util.function.Function;
import org.telegram.messenger.MessageObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class DeleteMessagesBottomSheet$$ExternalSyntheticLambda3 implements Function {
    public static final /* synthetic */ DeleteMessagesBottomSheet$$ExternalSyntheticLambda3 INSTANCE = new DeleteMessagesBottomSheet$$ExternalSyntheticLambda3();

    private /* synthetic */ DeleteMessagesBottomSheet$$ExternalSyntheticLambda3() {
    }

    @Override // j$.util.function.Function
    public /* synthetic */ Function andThen(Function function) {
        return Function.-CC.$default$andThen(this, function);
    }

    @Override // j$.util.function.Function
    public final Object apply(Object obj) {
        return Integer.valueOf(((MessageObject) obj).getId());
    }

    @Override // j$.util.function.Function
    public /* synthetic */ Function compose(Function function) {
        return Function.-CC.$default$compose(this, function);
    }
}
