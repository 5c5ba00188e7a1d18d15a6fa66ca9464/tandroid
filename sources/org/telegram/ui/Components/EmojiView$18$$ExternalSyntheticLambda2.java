package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.Components.EmojiView;
/* loaded from: classes4.dex */
public final /* synthetic */ class EmojiView$18$$ExternalSyntheticLambda2 implements RequestDelegate {
    public static final /* synthetic */ EmojiView$18$$ExternalSyntheticLambda2 INSTANCE = new EmojiView$18$$ExternalSyntheticLambda2();

    private /* synthetic */ EmojiView$18$$ExternalSyntheticLambda2() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        EmojiView.18.lambda$sendReorder$2(tLObject, tLRPC$TL_error);
    }
}
