package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$TL_contact;
/* loaded from: classes.dex */
public final /* synthetic */ class ContactsController$$ExternalSyntheticLambda52 implements Comparator {
    public static final /* synthetic */ ContactsController$$ExternalSyntheticLambda52 INSTANCE = new ContactsController$$ExternalSyntheticLambda52();

    private /* synthetic */ ContactsController$$ExternalSyntheticLambda52() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getContactsHash$25;
        lambda$getContactsHash$25 = ContactsController.lambda$getContactsHash$25((TLRPC$TL_contact) obj, (TLRPC$TL_contact) obj2);
        return lambda$getContactsHash$25;
    }
}
