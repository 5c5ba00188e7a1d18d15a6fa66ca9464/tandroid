package org.telegram.messenger;

import com.google.android.gms.tasks.OnSuccessListener;
import org.telegram.messenger.LanguageDetector;
/* loaded from: classes.dex */
public final /* synthetic */ class LanguageDetector$$ExternalSyntheticLambda1 implements OnSuccessListener {
    public final /* synthetic */ LanguageDetector.StringCallback f$0;

    public /* synthetic */ LanguageDetector$$ExternalSyntheticLambda1(LanguageDetector.StringCallback stringCallback) {
        this.f$0 = stringCallback;
    }

    @Override // com.google.android.gms.tasks.OnSuccessListener
    public final void onSuccess(Object obj) {
        LanguageDetector.lambda$detectLanguage$0(this.f$0, (String) obj);
    }
}
