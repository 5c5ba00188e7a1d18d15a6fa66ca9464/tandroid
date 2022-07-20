package org.telegram.ui;

import android.content.DialogInterface;
import java.util.ArrayList;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChannelAdminLogActivity$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ChannelAdminLogActivity f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ ChannelAdminLogActivity$$ExternalSyntheticLambda1(ChannelAdminLogActivity channelAdminLogActivity, ArrayList arrayList) {
        this.f$0 = channelAdminLogActivity;
        this.f$1 = arrayList;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$createMenu$9(this.f$1, dialogInterface, i);
    }
}
