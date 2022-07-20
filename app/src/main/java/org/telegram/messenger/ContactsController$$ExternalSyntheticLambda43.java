package org.telegram.messenger;

import androidx.collection.LongSparseArray;
import java.util.Comparator;
import org.telegram.tgnet.TLRPC$TL_contact;
/* loaded from: classes.dex */
public final /* synthetic */ class ContactsController$$ExternalSyntheticLambda43 implements Comparator {
    public final /* synthetic */ LongSparseArray f$0;

    public /* synthetic */ ContactsController$$ExternalSyntheticLambda43(LongSparseArray longSparseArray) {
        this.f$0 = longSparseArray;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processLoadedContacts$30;
        lambda$processLoadedContacts$30 = ContactsController.lambda$processLoadedContacts$30(this.f$0, (TLRPC$TL_contact) obj, (TLRPC$TL_contact) obj2);
        return lambda$processLoadedContacts$30;
    }
}
