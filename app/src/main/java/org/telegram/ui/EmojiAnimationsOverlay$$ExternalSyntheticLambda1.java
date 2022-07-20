package org.telegram.ui;

import org.telegram.messenger.MessageObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiAnimationsOverlay$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ EmojiAnimationsOverlay f$0;
    public final /* synthetic */ MessageObject f$1;

    public /* synthetic */ EmojiAnimationsOverlay$$ExternalSyntheticLambda1(EmojiAnimationsOverlay emojiAnimationsOverlay, MessageObject messageObject) {
        this.f$0 = emojiAnimationsOverlay;
        this.f$1 = messageObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$showStickerSetBulletin$3(this.f$1);
    }
}
