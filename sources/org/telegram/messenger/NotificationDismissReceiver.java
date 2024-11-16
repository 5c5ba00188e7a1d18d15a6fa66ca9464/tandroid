package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/* loaded from: classes3.dex */
public class NotificationDismissReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        StringBuilder sb;
        SharedPreferences.Editor edit;
        if (intent == null) {
            return;
        }
        int intExtra = intent.getIntExtra("currentAccount", UserConfig.selectedAccount);
        if (UserConfig.isValidAccount(intExtra)) {
            long longExtra = intent.getLongExtra("dialogId", 0L);
            int intExtra2 = intent.getIntExtra("messageDate", 0);
            if (intent.hasExtra("story") && intent.getBooleanExtra("story", false)) {
                NotificationsController.getInstance(intExtra).processIgnoreStories();
                return;
            }
            if (intent.hasExtra("storyReaction") && intent.getBooleanExtra("storyReaction", false)) {
                NotificationsController.getInstance(intExtra).processIgnoreStoryReactions();
                return;
            }
            String str = "dismissDate";
            if (longExtra == 0) {
                sb = new StringBuilder();
                sb.append("set dismissDate of global to ");
                sb.append(intExtra2);
                FileLog.d(sb.toString());
                edit = MessagesController.getNotificationsSettings(intExtra).edit();
            } else {
                sb = new StringBuilder();
                sb.append("set dismissDate of ");
                sb.append(longExtra);
                sb.append(" to ");
                sb.append(intExtra2);
                FileLog.d(sb.toString());
                edit = MessagesController.getNotificationsSettings(intExtra).edit();
                str = "dismissDate" + longExtra;
            }
            edit.putInt(str, intExtra2).commit();
        }
    }
}
