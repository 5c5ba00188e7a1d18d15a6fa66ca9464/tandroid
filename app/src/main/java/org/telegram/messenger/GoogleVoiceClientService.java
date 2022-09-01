package org.telegram.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.search.verification.client.SearchActionVerificationClientService;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$ReplyMarkup;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$WebPage;
/* loaded from: classes.dex */
public class GoogleVoiceClientService extends SearchActionVerificationClientService {
    @Override // com.google.android.search.verification.client.SearchActionVerificationClientService
    public void performAction(final Intent intent, boolean z, Bundle bundle) {
        if (!z) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.GoogleVoiceClientService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GoogleVoiceClientService.lambda$performAction$0(intent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$performAction$0(Intent intent) {
        try {
            int i = UserConfig.selectedAccount;
            ApplicationLoader.postInitApplication();
            if (!AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
                String stringExtra = intent.getStringExtra("android.intent.extra.TEXT");
                if (TextUtils.isEmpty(stringExtra)) {
                    return;
                }
                String stringExtra2 = intent.getStringExtra("com.google.android.voicesearch.extra.RECIPIENT_CONTACT_URI");
                long parseLong = Long.parseLong(intent.getStringExtra("com.google.android.voicesearch.extra.RECIPIENT_CONTACT_CHAT_ID"));
                TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(parseLong));
                if (user == null && (user = MessagesStorage.getInstance(i).getUserSync(parseLong)) != null) {
                    MessagesController.getInstance(i).putUser(user, true);
                }
                if (user == null) {
                    return;
                }
                ContactsController.getInstance(i).markAsContacted(stringExtra2);
                SendMessagesHelper.getInstance(i).sendMessage(stringExtra, user.id, (MessageObject) null, (MessageObject) null, (TLRPC$WebPage) null, true, (ArrayList<TLRPC$MessageEntity>) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, true, 0, (MessageObject.SendAnimationData) null, false);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }
}
