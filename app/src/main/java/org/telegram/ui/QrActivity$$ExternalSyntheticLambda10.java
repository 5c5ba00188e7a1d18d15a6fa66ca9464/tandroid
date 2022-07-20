package org.telegram.ui;

import org.telegram.ui.ActionBar.EmojiThemes;
import org.telegram.ui.QrActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class QrActivity$$ExternalSyntheticLambda10 implements QrActivity.OnItemSelectedListener {
    public final /* synthetic */ QrActivity f$0;

    public /* synthetic */ QrActivity$$ExternalSyntheticLambda10(QrActivity qrActivity) {
        this.f$0 = qrActivity;
    }

    @Override // org.telegram.ui.QrActivity.OnItemSelectedListener
    public final void onItemSelected(EmojiThemes emojiThemes, int i) {
        this.f$0.lambda$createView$2(emojiThemes, i);
    }
}
