package org.telegram.messenger;

import org.telegram.messenger.SendMessagesHelper;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_startHistoryImport;
/* loaded from: classes.dex */
public final /* synthetic */ class SendMessagesHelper$ImportingHistory$3$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SendMessagesHelper.ImportingHistory.AnonymousClass3 f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ TLRPC$TL_messages_startHistoryImport f$2;

    public /* synthetic */ SendMessagesHelper$ImportingHistory$3$$ExternalSyntheticLambda0(SendMessagesHelper.ImportingHistory.AnonymousClass3 anonymousClass3, TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_messages_startHistoryImport tLRPC$TL_messages_startHistoryImport) {
        this.f$0 = anonymousClass3;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = tLRPC$TL_messages_startHistoryImport;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$run$0(this.f$1, this.f$2);
    }
}
