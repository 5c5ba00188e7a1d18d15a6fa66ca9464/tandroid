package com.google.firebase.messaging;

import android.content.Intent;
import android.os.Binder;
import android.os.Process;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.WithinAppServiceConnection;

/* loaded from: classes.dex */
class WithinAppServiceBinder extends Binder {
    private final IntentHandler intentHandler;

    interface IntentHandler {
        Task handle(Intent intent);
    }

    WithinAppServiceBinder(IntentHandler intentHandler) {
        this.intentHandler = intentHandler;
    }

    void send(final WithinAppServiceConnection.BindRequest bindRequest) {
        if (Binder.getCallingUid() != Process.myUid()) {
            throw new SecurityException("Binding only allowed within app");
        }
        if (Log.isLoggable("FirebaseMessaging", 3)) {
            Log.d("FirebaseMessaging", "service received new intent via bind strategy");
        }
        this.intentHandler.handle(bindRequest.intent).addOnCompleteListener(WithinAppServiceBinder$$Lambda$0.$instance, new OnCompleteListener(bindRequest) { // from class: com.google.firebase.messaging.WithinAppServiceBinder$$Lambda$1
            private final WithinAppServiceConnection.BindRequest arg$1;

            {
                this.arg$1 = bindRequest;
            }

            @Override // com.google.android.gms.tasks.OnCompleteListener
            public void onComplete(Task task) {
                this.arg$1.finish();
            }
        });
    }
}
