package org.telegram.ui.Components;

import java.util.ArrayList;
import org.telegram.ui.DialogsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class AudioPlayerAlert$$ExternalSyntheticLambda12 implements DialogsActivity.DialogsActivityDelegate {
    public final /* synthetic */ AudioPlayerAlert f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ AudioPlayerAlert$$ExternalSyntheticLambda12(AudioPlayerAlert audioPlayerAlert, ArrayList arrayList) {
        this.f$0 = audioPlayerAlert;
        this.f$1 = arrayList;
    }

    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        this.f$0.lambda$onSubItemClick$9(this.f$1, dialogsActivity, arrayList, charSequence, z);
    }
}
