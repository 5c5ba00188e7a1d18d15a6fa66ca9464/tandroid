package org.telegram.ui;

import org.telegram.ui.ActionBar.EmojiThemes;
import org.telegram.ui.ChatActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$ThemeDelegate$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ ChatActivity.ThemeDelegate f$0;
    public final /* synthetic */ EmojiThemes f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ ChatActivity$ThemeDelegate$$ExternalSyntheticLambda6(ChatActivity.ThemeDelegate themeDelegate, EmojiThemes emojiThemes, boolean z) {
        this.f$0 = themeDelegate;
        this.f$1 = emojiThemes;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setCurrentTheme$1(this.f$1, this.f$2);
    }
}
