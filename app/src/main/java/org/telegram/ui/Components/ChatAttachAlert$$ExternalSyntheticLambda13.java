package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlert$$ExternalSyntheticLambda13 implements View.OnLongClickListener {
    public final /* synthetic */ ChatAttachAlert f$0;
    public final /* synthetic */ Theme.ResourcesProvider f$1;

    public /* synthetic */ ChatAttachAlert$$ExternalSyntheticLambda13(ChatAttachAlert chatAttachAlert, Theme.ResourcesProvider resourcesProvider) {
        this.f$0 = chatAttachAlert;
        this.f$1 = resourcesProvider;
    }

    @Override // android.view.View.OnLongClickListener
    public final boolean onLongClick(View view) {
        boolean lambda$new$16;
        lambda$new$16 = this.f$0.lambda$new$16(this.f$1, view);
        return lambda$new$16;
    }
}
