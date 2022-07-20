package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.GroupCreateActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$$ExternalSyntheticLambda41 implements GroupCreateActivity.ContactsAddActivityDelegate {
    public final /* synthetic */ ProfileActivity f$0;

    public /* synthetic */ ProfileActivity$$ExternalSyntheticLambda41(ProfileActivity profileActivity) {
        this.f$0 = profileActivity;
    }

    @Override // org.telegram.ui.GroupCreateActivity.ContactsAddActivityDelegate
    public final void didSelectUsers(ArrayList arrayList, int i) {
        this.f$0.lambda$openAddMember$28(arrayList, i);
    }

    @Override // org.telegram.ui.GroupCreateActivity.ContactsAddActivityDelegate
    public /* synthetic */ void needAddBot(TLRPC$User tLRPC$User) {
        GroupCreateActivity.ContactsAddActivityDelegate.CC.$default$needAddBot(this, tLRPC$User);
    }
}
