package org.telegram.messenger;

import org.telegram.messenger.NotificationCenter;
/* loaded from: classes.dex */
public final /* synthetic */ class ContactsLoadingObserver$$ExternalSyntheticLambda1 implements NotificationCenter.NotificationCenterDelegate {
    public final /* synthetic */ ContactsLoadingObserver f$0;

    public /* synthetic */ ContactsLoadingObserver$$ExternalSyntheticLambda1(ContactsLoadingObserver contactsLoadingObserver) {
        this.f$0 = contactsLoadingObserver;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public final void didReceivedNotification(int i, int i2, Object[] objArr) {
        this.f$0.lambda$new$0(i, i2, objArr);
    }
}
