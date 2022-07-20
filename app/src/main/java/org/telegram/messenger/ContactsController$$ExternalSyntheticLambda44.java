package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$TL_contact;
/* loaded from: classes.dex */
public final /* synthetic */ class ContactsController$$ExternalSyntheticLambda44 implements Comparator {
    public final /* synthetic */ ContactsController f$0;

    public /* synthetic */ ContactsController$$ExternalSyntheticLambda44(ContactsController contactsController) {
        this.f$0 = contactsController;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$buildContactsSectionsArrays$43;
        lambda$buildContactsSectionsArrays$43 = this.f$0.lambda$buildContactsSectionsArrays$43((TLRPC$TL_contact) obj, (TLRPC$TL_contact) obj2);
        return lambda$buildContactsSectionsArrays$43;
    }
}
