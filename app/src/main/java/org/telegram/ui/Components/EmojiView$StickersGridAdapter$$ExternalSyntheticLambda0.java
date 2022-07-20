package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.ui.Components.EmojiView;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiView$StickersGridAdapter$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ EmojiView.StickersGridAdapter f$0;

    public /* synthetic */ EmojiView$StickersGridAdapter$$ExternalSyntheticLambda0(EmojiView.StickersGridAdapter stickersGridAdapter) {
        this.f$0 = stickersGridAdapter;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onCreateViewHolder$0(dialogInterface, i);
    }
}
