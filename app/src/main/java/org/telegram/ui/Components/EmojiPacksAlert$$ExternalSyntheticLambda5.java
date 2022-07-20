package org.telegram.ui.Components;

import java.util.ArrayList;
import org.telegram.messenger.Utilities;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiPacksAlert$$ExternalSyntheticLambda5 implements Utilities.Callback {
    public final /* synthetic */ Runnable f$0;

    public /* synthetic */ EmojiPacksAlert$$ExternalSyntheticLambda5(Runnable runnable) {
        this.f$0 = runnable;
    }

    @Override // org.telegram.messenger.Utilities.Callback
    public final void run(Object obj) {
        EmojiPacksAlert.lambda$installSet$1(this.f$0, (ArrayList) obj);
    }
}
