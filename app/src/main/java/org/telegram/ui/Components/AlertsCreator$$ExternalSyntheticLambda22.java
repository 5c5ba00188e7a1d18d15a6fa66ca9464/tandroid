package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.tgnet.TLRPC$TL_langPackLanguage;
import org.telegram.ui.LaunchActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda22 implements DialogInterface.OnClickListener {
    public final /* synthetic */ TLRPC$TL_langPackLanguage f$0;
    public final /* synthetic */ LaunchActivity f$1;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda22(TLRPC$TL_langPackLanguage tLRPC$TL_langPackLanguage, LaunchActivity launchActivity) {
        this.f$0 = tLRPC$TL_langPackLanguage;
        this.f$1 = launchActivity;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createLanguageAlert$8(this.f$0, this.f$1, dialogInterface, i);
    }
}
