package org.telegram.ui.Components;

import android.view.View;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiTabsStrip$$ExternalSyntheticLambda4 implements View.OnClickListener {
    public final /* synthetic */ EmojiTabsStrip f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ EmojiTabsStrip$$ExternalSyntheticLambda4(EmojiTabsStrip emojiTabsStrip, int i) {
        this.f$0 = emojiTabsStrip;
        this.f$1 = i;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$updateClickListeners$2(this.f$1, view);
    }
}
