package org.telegram.ui.Adapters;

import androidx.collection.LongSparseArray;
import java.util.ArrayList;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.Adapters.MentionsAdapter;
/* loaded from: classes3.dex */
public final /* synthetic */ class MentionsAdapter$7$$ExternalSyntheticLambda1 implements RequestDelegate {
    public final /* synthetic */ MentionsAdapter.AnonymousClass7 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ LongSparseArray f$3;
    public final /* synthetic */ MessagesController f$4;

    public /* synthetic */ MentionsAdapter$7$$ExternalSyntheticLambda1(MentionsAdapter.AnonymousClass7 anonymousClass7, int i, ArrayList arrayList, LongSparseArray longSparseArray, MessagesController messagesController) {
        this.f$0 = anonymousClass7;
        this.f$1 = i;
        this.f$2 = arrayList;
        this.f$3 = longSparseArray;
        this.f$4 = messagesController;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$run$1(this.f$1, this.f$2, this.f$3, this.f$4, tLObject, tLRPC$TL_error);
    }
}
