package org.telegram.ui.Components;

import android.app.Activity;
import android.net.Uri;
import java.io.File;
import org.telegram.ui.Components.ChatActivityEnterView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivityEnterView$12$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ ChatActivityEnterView.AnonymousClass12 f$0;
    public final /* synthetic */ Activity f$1;
    public final /* synthetic */ Uri f$2;
    public final /* synthetic */ File f$3;

    public /* synthetic */ ChatActivityEnterView$12$$ExternalSyntheticLambda2(ChatActivityEnterView.AnonymousClass12 anonymousClass12, Activity activity, Uri uri, File file) {
        this.f$0 = anonymousClass12;
        this.f$1 = activity;
        this.f$2 = uri;
        this.f$3 = file;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$editPhoto$4(this.f$1, this.f$2, this.f$3);
    }
}
