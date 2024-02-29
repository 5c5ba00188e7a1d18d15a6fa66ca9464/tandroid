package org.telegram.ui.Cells;

import j$.util.function.ToIntFunction;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.ui.Cells.DialogCell;
/* loaded from: classes4.dex */
public final /* synthetic */ class DialogCell$ForumFormattedNames$$ExternalSyntheticLambda0 implements ToIntFunction {
    public static final /* synthetic */ DialogCell$ForumFormattedNames$$ExternalSyntheticLambda0 INSTANCE = new DialogCell$ForumFormattedNames$$ExternalSyntheticLambda0();

    private /* synthetic */ DialogCell$ForumFormattedNames$$ExternalSyntheticLambda0() {
    }

    @Override // j$.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        int lambda$formatTopicsNames$0;
        lambda$formatTopicsNames$0 = DialogCell.ForumFormattedNames.lambda$formatTopicsNames$0((TLRPC$TL_forumTopic) obj);
        return lambda$formatTopicsNames$0;
    }
}
