package org.telegram.messenger.voip;

import android.content.DialogInterface;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ VoIPService f$0;

    public /* synthetic */ VoIPService$$ExternalSyntheticLambda0(VoIPService voIPService) {
        this.f$0 = voIPService;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$toggleSpeakerphoneOrShowRouteSheet$62(dialogInterface, i);
    }
}
