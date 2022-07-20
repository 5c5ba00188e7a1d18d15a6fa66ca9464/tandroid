package com.google.firebase;

import android.content.Context;
import com.google.firebase.inject.Provider;
import com.google.firebase.internal.DataCollectionConfigStorage;
/* loaded from: classes.dex */
public final /* synthetic */ class FirebaseApp$$ExternalSyntheticLambda0 implements Provider {
    public final /* synthetic */ FirebaseApp f$0;
    public final /* synthetic */ Context f$1;

    public /* synthetic */ FirebaseApp$$ExternalSyntheticLambda0(FirebaseApp firebaseApp, Context context) {
        this.f$0 = firebaseApp;
        this.f$1 = context;
    }

    @Override // com.google.firebase.inject.Provider
    public final Object get() {
        DataCollectionConfigStorage lambda$new$0;
        lambda$new$0 = this.f$0.lambda$new$0(this.f$1);
        return lambda$new$0;
    }
}
