package org.telegram.ui;

import android.widget.EditText;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$8$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ LoginActivity.AnonymousClass8 f$0;
    public final /* synthetic */ EditText f$1;
    public final /* synthetic */ AtomicReference f$2;

    public /* synthetic */ LoginActivity$8$$ExternalSyntheticLambda0(LoginActivity.AnonymousClass8 anonymousClass8, EditText editText, AtomicReference atomicReference) {
        this.f$0 = anonymousClass8;
        this.f$1 = editText;
        this.f$2 = atomicReference;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$beforeTextChanged$0(this.f$1, this.f$2);
    }
}
