package org.telegram.ui.Adapters;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class MentionsAdapter$$ExternalSyntheticLambda2 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ MentionsAdapter f$0;
    public final /* synthetic */ boolean[] f$1;

    public /* synthetic */ MentionsAdapter$$ExternalSyntheticLambda2(MentionsAdapter mentionsAdapter, boolean[] zArr) {
        this.f$0 = mentionsAdapter;
        this.f$1 = zArr;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$processFoundUser$4(this.f$1, dialogInterface);
    }
}
