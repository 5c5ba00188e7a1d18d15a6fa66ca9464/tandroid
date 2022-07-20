package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.ui.Components.EditTextEmoji;
/* loaded from: classes3.dex */
public final /* synthetic */ class EditTextEmoji$5$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ EditTextEmoji.AnonymousClass5 f$0;

    public /* synthetic */ EditTextEmoji$5$$ExternalSyntheticLambda0(EditTextEmoji.AnonymousClass5 anonymousClass5) {
        this.f$0 = anonymousClass5;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onClearEmojiRecent$0(dialogInterface, i);
    }
}
