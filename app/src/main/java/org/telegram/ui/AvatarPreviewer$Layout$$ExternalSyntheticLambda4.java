package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.AvatarPreviewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class AvatarPreviewer$Layout$$ExternalSyntheticLambda4 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ AvatarPreviewer.Layout f$0;

    public /* synthetic */ AvatarPreviewer$Layout$$ExternalSyntheticLambda4(AvatarPreviewer.Layout layout) {
        this.f$0 = layout;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$showBottomSheet$2(dialogInterface);
    }
}
