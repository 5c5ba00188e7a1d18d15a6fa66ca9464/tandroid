package org.telegram.messenger;

import com.google.android.gms.tasks.OnSuccessListener;
/* loaded from: classes.dex */
public final /* synthetic */ class AndroidUtilities$$ExternalSyntheticLambda10 implements OnSuccessListener {
    public static final /* synthetic */ AndroidUtilities$$ExternalSyntheticLambda10 INSTANCE = new AndroidUtilities$$ExternalSyntheticLambda10();

    private /* synthetic */ AndroidUtilities$$ExternalSyntheticLambda10() {
    }

    @Override // com.google.android.gms.tasks.OnSuccessListener
    public final void onSuccess(Object obj) {
        AndroidUtilities.lambda$setWaitingForSms$8((Void) obj);
    }
}
