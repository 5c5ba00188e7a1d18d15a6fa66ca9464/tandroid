package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.core.app.RemoteInput;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$User;
/* loaded from: classes.dex */
public class WearReplyReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        ApplicationLoader.postInitApplication();
        Bundle resultsFromIntent = RemoteInput.getResultsFromIntent(intent);
        if (resultsFromIntent == null) {
            return;
        }
        CharSequence charSequence = resultsFromIntent.getCharSequence("extra_voice_reply");
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        long longExtra = intent.getLongExtra("dialog_id", 0L);
        int intExtra = intent.getIntExtra("max_id", 0);
        int intExtra2 = intent.getIntExtra("currentAccount", 0);
        if (longExtra == 0 || intExtra == 0 || !UserConfig.isValidAccount(intExtra2)) {
            return;
        }
        AccountInstance accountInstance = AccountInstance.getInstance(intExtra2);
        if (DialogObject.isUserDialog(longExtra)) {
            if (accountInstance.getMessagesController().getUser(Long.valueOf(longExtra)) == null) {
                Utilities.globalQueue.postRunnable(new WearReplyReceiver$$ExternalSyntheticLambda0(this, accountInstance, longExtra, charSequence, intExtra));
                return;
            }
        } else if (DialogObject.isChatDialog(longExtra) && accountInstance.getMessagesController().getChat(Long.valueOf(-longExtra)) == null) {
            Utilities.globalQueue.postRunnable(new WearReplyReceiver$$ExternalSyntheticLambda1(this, accountInstance, longExtra, charSequence, intExtra));
            return;
        }
        sendMessage(accountInstance, charSequence, longExtra, intExtra);
    }

    public /* synthetic */ void lambda$onReceive$1(AccountInstance accountInstance, long j, CharSequence charSequence, int i) {
        AndroidUtilities.runOnUIThread(new WearReplyReceiver$$ExternalSyntheticLambda3(this, accountInstance, accountInstance.getMessagesStorage().getUserSync(j), charSequence, j, i));
    }

    public /* synthetic */ void lambda$onReceive$0(AccountInstance accountInstance, TLRPC$User tLRPC$User, CharSequence charSequence, long j, int i) {
        accountInstance.getMessagesController().putUser(tLRPC$User, true);
        sendMessage(accountInstance, charSequence, j, i);
    }

    public /* synthetic */ void lambda$onReceive$3(AccountInstance accountInstance, long j, CharSequence charSequence, int i) {
        AndroidUtilities.runOnUIThread(new WearReplyReceiver$$ExternalSyntheticLambda2(this, accountInstance, accountInstance.getMessagesStorage().getChatSync(-j), charSequence, j, i));
    }

    public /* synthetic */ void lambda$onReceive$2(AccountInstance accountInstance, TLRPC$Chat tLRPC$Chat, CharSequence charSequence, long j, int i) {
        accountInstance.getMessagesController().putChat(tLRPC$Chat, true);
        sendMessage(accountInstance, charSequence, j, i);
    }

    private void sendMessage(AccountInstance accountInstance, CharSequence charSequence, long j, int i) {
        accountInstance.getSendMessagesHelper().sendMessage(charSequence.toString(), j, null, null, null, true, null, null, null, true, 0, null);
        accountInstance.getMessagesController().markDialogAsRead(j, i, i, 0, false, 0, 0, true, 0);
    }
}
