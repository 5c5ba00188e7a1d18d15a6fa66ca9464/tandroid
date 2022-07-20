package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.ChannelAdminLogActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChannelAdminLogActivity$ChatActivityAdapter$1$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ChannelAdminLogActivity.ChatActivityAdapter.AnonymousClass1 f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ ChannelAdminLogActivity$ChatActivityAdapter$1$$ExternalSyntheticLambda0(ChannelAdminLogActivity.ChatActivityAdapter.AnonymousClass1 anonymousClass1, String str) {
        this.f$0 = anonymousClass1;
        this.f$1 = str;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$didPressUrl$1(this.f$1, dialogInterface, i);
    }
}
