package org.telegram.ui.Components;

import java.util.Comparator;
import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupVoipInviteAlert$$ExternalSyntheticLambda2 implements Comparator {
    public final /* synthetic */ GroupVoipInviteAlert f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ GroupVoipInviteAlert$$ExternalSyntheticLambda2(GroupVoipInviteAlert groupVoipInviteAlert, int i) {
        this.f$0 = groupVoipInviteAlert;
        this.f$1 = i;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$loadChatParticipants$2;
        lambda$loadChatParticipants$2 = this.f$0.lambda$loadChatParticipants$2(this.f$1, (TLObject) obj, (TLObject) obj2);
        return lambda$loadChatParticipants$2;
    }
}
