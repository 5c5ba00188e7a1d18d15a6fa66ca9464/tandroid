package org.telegram.ui.Components;
/* loaded from: classes3.dex */
public final /* synthetic */ class AnimatedPhoneNumberEditText$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ AnimatedPhoneNumberEditText f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ AnimatedPhoneNumberEditText$$ExternalSyntheticLambda1(AnimatedPhoneNumberEditText animatedPhoneNumberEditText, boolean z, String str) {
        this.f$0 = animatedPhoneNumberEditText;
        this.f$1 = z;
        this.f$2 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setHintText$0(this.f$1, this.f$2);
    }
}
