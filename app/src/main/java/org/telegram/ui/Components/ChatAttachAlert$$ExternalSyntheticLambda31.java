package org.telegram.ui.Components;

import java.util.ArrayList;
import org.telegram.ui.Components.ChatAttachAlertAudioLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlert$$ExternalSyntheticLambda31 implements ChatAttachAlertAudioLayout.AudioSelectDelegate {
    public final /* synthetic */ ChatAttachAlert f$0;

    public /* synthetic */ ChatAttachAlert$$ExternalSyntheticLambda31(ChatAttachAlert chatAttachAlert) {
        this.f$0 = chatAttachAlert;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlertAudioLayout.AudioSelectDelegate
    public final void didSelectAudio(ArrayList arrayList, CharSequence charSequence, boolean z, int i) {
        this.f$0.lambda$openAudioLayout$25(arrayList, charSequence, z, i);
    }
}
