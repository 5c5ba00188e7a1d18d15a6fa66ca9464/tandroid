package org.telegram.ui;
/* loaded from: classes3.dex */
public final /* synthetic */ class TwoStepVerificationSetupActivity$$ExternalSyntheticLambda19 implements Runnable {
    public final /* synthetic */ CodeNumberField f$0;

    public /* synthetic */ TwoStepVerificationSetupActivity$$ExternalSyntheticLambda19(CodeNumberField codeNumberField) {
        this.f$0 = codeNumberField;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.animateSuccessProgress(1.0f);
    }
}
