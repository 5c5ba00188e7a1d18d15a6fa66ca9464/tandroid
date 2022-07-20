package org.telegram.ui;

import org.telegram.messenger.LanguageDetector;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$$ExternalSyntheticLambda30 implements LanguageDetector.ExceptionCallback {
    public final /* synthetic */ Runnable f$0;

    public /* synthetic */ ProfileActivity$$ExternalSyntheticLambda30(Runnable runnable) {
        this.f$0 = runnable;
    }

    @Override // org.telegram.messenger.LanguageDetector.ExceptionCallback
    public final void run(Exception exc) {
        ProfileActivity.lambda$processOnClickOrPress$24(this.f$0, exc);
    }
}
