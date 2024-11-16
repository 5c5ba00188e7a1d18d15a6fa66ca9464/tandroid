package org.telegram.ui;

import org.telegram.messenger.Utilities;
import org.telegram.tgnet.tl.TL_chatlists;

/* loaded from: classes4.dex */
public final /* synthetic */ class FilterCreateActivity$$ExternalSyntheticLambda22 implements Utilities.Callback {
    public final /* synthetic */ FilterCreateActivity f$0;

    public /* synthetic */ FilterCreateActivity$$ExternalSyntheticLambda22(FilterCreateActivity filterCreateActivity) {
        this.f$0 = filterCreateActivity;
    }

    @Override // org.telegram.messenger.Utilities.Callback
    public final void run(Object obj) {
        this.f$0.onEdit((TL_chatlists.TL_exportedChatlistInvite) obj);
    }
}
