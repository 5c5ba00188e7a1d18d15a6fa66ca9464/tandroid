package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.ui.Components.SharedMediaLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class SharedMediaLayout$30$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ SharedMediaLayout.AnonymousClass30 f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ SharedMediaLayout$30$$ExternalSyntheticLambda0(SharedMediaLayout.AnonymousClass30 anonymousClass30, String str) {
        this.f$0 = anonymousClass30;
        this.f$1 = str;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onLinkPress$0(this.f$1, dialogInterface, i);
    }
}
