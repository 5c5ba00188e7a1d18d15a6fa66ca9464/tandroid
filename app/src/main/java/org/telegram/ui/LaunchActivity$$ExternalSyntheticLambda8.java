package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.ActionBar.BaseFragment;
/* loaded from: classes3.dex */
public final /* synthetic */ class LaunchActivity$$ExternalSyntheticLambda8 implements DialogInterface.OnClickListener {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ BaseFragment f$2;

    public /* synthetic */ LaunchActivity$$ExternalSyntheticLambda8(String str, String str2, BaseFragment baseFragment) {
        this.f$0 = str;
        this.f$1 = str2;
        this.f$2 = baseFragment;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        LaunchActivity.lambda$handleIntent$22(this.f$0, this.f$1, this.f$2, dialogInterface, i);
    }
}
