package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.ChannelAdminLogActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChannelAdminLogActivity$ChatActivityAdapter$3$$ExternalSyntheticLambda0 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ ChannelAdminLogActivity.ChatActivityAdapter.AnonymousClass3 f$0;
    public final /* synthetic */ boolean[] f$1;

    public /* synthetic */ ChannelAdminLogActivity$ChatActivityAdapter$3$$ExternalSyntheticLambda0(ChannelAdminLogActivity.ChatActivityAdapter.AnonymousClass3 anonymousClass3, boolean[] zArr) {
        this.f$0 = anonymousClass3;
        this.f$1 = zArr;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$needOpenInviteLink$0(this.f$1, dialogInterface);
    }
}
