package org.telegram.ui.Cells;

import android.view.Menu;
import org.telegram.messenger.LanguageDetector;
import org.telegram.ui.Cells.TextSelectionHelper;
/* loaded from: classes3.dex */
public final /* synthetic */ class TextSelectionHelper$4$$ExternalSyntheticLambda2 implements LanguageDetector.StringCallback {
    public final /* synthetic */ TextSelectionHelper.AnonymousClass4 f$0;
    public final /* synthetic */ Menu f$1;

    public /* synthetic */ TextSelectionHelper$4$$ExternalSyntheticLambda2(TextSelectionHelper.AnonymousClass4 anonymousClass4, Menu menu) {
        this.f$0 = anonymousClass4;
        this.f$1 = menu;
    }

    @Override // org.telegram.messenger.LanguageDetector.StringCallback
    public final void run(String str) {
        this.f$0.lambda$onPrepareActionMode$0(this.f$1, str);
    }
}
