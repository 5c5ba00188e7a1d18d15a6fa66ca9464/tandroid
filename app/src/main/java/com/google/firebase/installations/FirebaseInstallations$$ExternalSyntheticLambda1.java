package com.google.firebase.installations;
/* loaded from: classes.dex */
public final /* synthetic */ class FirebaseInstallations$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ FirebaseInstallations f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ FirebaseInstallations$$ExternalSyntheticLambda1(FirebaseInstallations firebaseInstallations, boolean z) {
        this.f$0 = firebaseInstallations;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$doRegistrationOrRefresh$2(this.f$1);
    }
}
