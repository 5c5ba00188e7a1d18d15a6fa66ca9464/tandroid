package org.telegram.ui.Components;

import java.util.Comparator;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupVoipInviteAlert$$ExternalSyntheticLambda1 implements Comparator {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ GroupVoipInviteAlert$$ExternalSyntheticLambda1(MessagesController messagesController, int i) {
        this.f$0 = messagesController;
        this.f$1 = i;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$fillContacts$1;
        lambda$fillContacts$1 = GroupVoipInviteAlert.lambda$fillContacts$1(this.f$0, this.f$1, (TLObject) obj, (TLObject) obj2);
        return lambda$fillContacts$1;
    }
}
