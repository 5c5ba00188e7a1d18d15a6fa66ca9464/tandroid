package com.google.firebase.messaging;

import android.content.Intent;
import com.google.android.gms.tasks.TaskCompletionSource;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.firebase:firebase-messaging@@22.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class EnhancedIntentService$$Lambda$0 implements Runnable {
    private final EnhancedIntentService arg$1;
    private final Intent arg$2;
    private final TaskCompletionSource arg$3;

    public EnhancedIntentService$$Lambda$0(EnhancedIntentService enhancedIntentService, Intent intent, TaskCompletionSource taskCompletionSource) {
        this.arg$1 = enhancedIntentService;
        this.arg$2 = intent;
        this.arg$3 = taskCompletionSource;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.arg$1.lambda$processIntent$0$EnhancedIntentService(this.arg$2, this.arg$3);
    }
}
