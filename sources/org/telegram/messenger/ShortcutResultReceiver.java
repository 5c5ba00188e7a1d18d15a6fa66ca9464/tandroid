package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.telegram.messenger.Utilities;

/* loaded from: classes3.dex */
public class ShortcutResultReceiver extends BroadcastReceiver {
    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onReceive$0(Utilities.Callback callback) {
        callback.run(Boolean.TRUE);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        int intExtra = intent.getIntExtra("account", UserConfig.selectedAccount);
        final Utilities.Callback<Boolean> remove = MediaDataController.getInstance(intExtra).shortcutCallbacks.remove(intent.getStringExtra("req_id"));
        if (remove != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ShortcutResultReceiver$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ShortcutResultReceiver.lambda$onReceive$0(Utilities.Callback.this);
                }
            });
        }
    }
}
