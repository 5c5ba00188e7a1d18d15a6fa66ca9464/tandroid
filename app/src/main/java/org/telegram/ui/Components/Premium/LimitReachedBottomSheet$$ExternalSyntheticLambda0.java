package org.telegram.ui.Components.Premium;

import android.content.DialogInterface;
import java.util.ArrayList;
/* loaded from: classes3.dex */
public final /* synthetic */ class LimitReachedBottomSheet$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ LimitReachedBottomSheet f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ LimitReachedBottomSheet$$ExternalSyntheticLambda0(LimitReachedBottomSheet limitReachedBottomSheet, ArrayList arrayList) {
        this.f$0 = limitReachedBottomSheet;
        this.f$1 = arrayList;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$revokeLinks$8(this.f$1, dialogInterface, i);
    }
}
