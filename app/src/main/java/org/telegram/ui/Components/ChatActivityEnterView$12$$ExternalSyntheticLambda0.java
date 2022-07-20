package org.telegram.ui.Components;

import android.os.Bundle;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.ChatActivityEnterView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivityEnterView$12$$ExternalSyntheticLambda0 implements InputConnectionCompat.OnCommitContentListener {
    public final /* synthetic */ ChatActivityEnterView.AnonymousClass12 f$0;
    public final /* synthetic */ Theme.ResourcesProvider f$1;

    public /* synthetic */ ChatActivityEnterView$12$$ExternalSyntheticLambda0(ChatActivityEnterView.AnonymousClass12 anonymousClass12, Theme.ResourcesProvider resourcesProvider) {
        this.f$0 = anonymousClass12;
        this.f$1 = resourcesProvider;
    }

    @Override // androidx.core.view.inputmethod.InputConnectionCompat.OnCommitContentListener
    public final boolean onCommitContent(InputContentInfoCompat inputContentInfoCompat, int i, Bundle bundle) {
        boolean lambda$onCreateInputConnection$1;
        lambda$onCreateInputConnection$1 = this.f$0.lambda$onCreateInputConnection$1(this.f$1, inputContentInfoCompat, i, bundle);
        return lambda$onCreateInputConnection$1;
    }
}
