package org.telegram.ui.Components;

import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.ui.Cells.ChatMessageCell;
/* loaded from: classes3.dex */
public final /* synthetic */ class SlotsDrawable$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ TLRPC$Document f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ MessageObject f$2;
    public final /* synthetic */ ChatMessageCell f$3;
    public final /* synthetic */ TLRPC$TL_messages_stickerSet f$4;

    public /* synthetic */ SlotsDrawable$$ExternalSyntheticLambda0(TLRPC$Document tLRPC$Document, int i, MessageObject messageObject, ChatMessageCell chatMessageCell, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        this.f$0 = tLRPC$Document;
        this.f$1 = i;
        this.f$2 = messageObject;
        this.f$3 = chatMessageCell;
        this.f$4 = tLRPC$TL_messages_stickerSet;
    }

    @Override // java.lang.Runnable
    public final void run() {
        SlotsDrawable.lambda$setBaseDice$2(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
