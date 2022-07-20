package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.DataUsageActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class DataUsageActivity$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ DataUsageActivity f$0;
    public final /* synthetic */ DataUsageActivity.ListAdapter f$1;

    public /* synthetic */ DataUsageActivity$$ExternalSyntheticLambda0(DataUsageActivity dataUsageActivity, DataUsageActivity.ListAdapter listAdapter) {
        this.f$0 = dataUsageActivity;
        this.f$1 = listAdapter;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$createView$1(this.f$1, dialogInterface, i);
    }
}
