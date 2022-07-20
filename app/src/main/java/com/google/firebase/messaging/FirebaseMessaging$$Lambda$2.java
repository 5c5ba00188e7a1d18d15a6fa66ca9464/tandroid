package com.google.firebase.messaging;

import com.google.android.gms.tasks.OnSuccessListener;
/* compiled from: com.google.firebase:firebase-messaging@@22.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class FirebaseMessaging$$Lambda$2 implements OnSuccessListener {
    private final FirebaseMessaging arg$1;

    public FirebaseMessaging$$Lambda$2(FirebaseMessaging firebaseMessaging) {
        this.arg$1 = firebaseMessaging;
    }

    @Override // com.google.android.gms.tasks.OnSuccessListener
    public void onSuccess(Object obj) {
        this.arg$1.lambda$new$1$FirebaseMessaging((TopicsSubscriber) obj);
    }
}
