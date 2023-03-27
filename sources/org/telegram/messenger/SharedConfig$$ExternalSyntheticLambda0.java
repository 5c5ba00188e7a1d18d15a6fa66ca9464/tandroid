package org.telegram.messenger;

import android.content.DialogInterface;
/* loaded from: classes.dex */
public final /* synthetic */ class SharedConfig$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public static final /* synthetic */ SharedConfig$$ExternalSyntheticLambda0 INSTANCE = new SharedConfig$$ExternalSyntheticLambda0();

    private /* synthetic */ SharedConfig$$ExternalSyntheticLambda0() {
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        SharedConfig.lambda$checkSdCard$1(dialogInterface, i);
    }
}
