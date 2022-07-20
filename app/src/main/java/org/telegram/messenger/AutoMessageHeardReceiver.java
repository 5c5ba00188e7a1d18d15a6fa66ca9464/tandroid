package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$User;
/* loaded from: classes.dex */
public class AutoMessageHeardReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        ApplicationLoader.postInitApplication();
        long longExtra = intent.getLongExtra("dialog_id", 0L);
        int intExtra = intent.getIntExtra("max_id", 0);
        int intExtra2 = intent.getIntExtra("currentAccount", 0);
        if (longExtra == 0 || intExtra == 0 || !UserConfig.isValidAccount(intExtra2)) {
            return;
        }
        AccountInstance accountInstance = AccountInstance.getInstance(intExtra2);
        if (DialogObject.isUserDialog(longExtra)) {
            if (accountInstance.getMessagesController().getUser(Long.valueOf(longExtra)) == null) {
                Utilities.globalQueue.postRunnable(new AutoMessageHeardReceiver$$ExternalSyntheticLambda1(accountInstance, longExtra, intExtra2, intExtra));
                return;
            }
        } else if (DialogObject.isChatDialog(longExtra) && accountInstance.getMessagesController().getChat(Long.valueOf(-longExtra)) == null) {
            Utilities.globalQueue.postRunnable(new AutoMessageHeardReceiver$$ExternalSyntheticLambda0(accountInstance, longExtra, intExtra2, intExtra));
            return;
        }
        MessagesController.getInstance(intExtra2).markDialogAsRead(longExtra, intExtra, intExtra, 0, false, 0, 0, true, 0);
    }

    public static /* synthetic */ void lambda$onReceive$1(AccountInstance accountInstance, long j, int i, int i2) {
        AndroidUtilities.runOnUIThread(new AutoMessageHeardReceiver$$ExternalSyntheticLambda3(accountInstance, accountInstance.getMessagesStorage().getUserSync(j), i, j, i2));
    }

    public static /* synthetic */ void lambda$onReceive$0(AccountInstance accountInstance, TLRPC$User tLRPC$User, int i, long j, int i2) {
        accountInstance.getMessagesController().putUser(tLRPC$User, true);
        MessagesController.getInstance(i).markDialogAsRead(j, i2, i2, 0, false, 0, 0, true, 0);
    }

    public static /* synthetic */ void lambda$onReceive$3(AccountInstance accountInstance, long j, int i, int i2) {
        AndroidUtilities.runOnUIThread(new AutoMessageHeardReceiver$$ExternalSyntheticLambda2(accountInstance, accountInstance.getMessagesStorage().getChatSync(-j), i, j, i2));
    }

    public static /* synthetic */ void lambda$onReceive$2(AccountInstance accountInstance, TLRPC$Chat tLRPC$Chat, int i, long j, int i2) {
        accountInstance.getMessagesController().putChat(tLRPC$Chat, true);
        MessagesController.getInstance(i).markDialogAsRead(j, i2, i2, 0, false, 0, 0, true, 0);
    }
}
